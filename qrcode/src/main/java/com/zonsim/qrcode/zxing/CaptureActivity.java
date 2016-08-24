/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zonsim.qrcode.zxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.zonsim.qrcode.R;
import com.zonsim.qrcode.zxing.camera.CameraManager;
import com.zonsim.qrcode.zxing.result.ResultHandler;
import com.zonsim.qrcode.zxing.result.ResultHandlerFactory;
import com.zonsim.qrcode.zxing.view.ViewfinderView;

import java.io.IOException;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback {
	
	private static final String TAG = "CaptureActivity";
	
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private TextView statusView;
	private View resultView;
	private Result lastResult;
	private boolean hasSurface;

	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;
	private Button mBtnBack;
	private CheckBox mBtnLight;
	private Button mBtnGallery;

	
	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}
	
	public Handler getHandler() {
		return handler;
	}
	
	CameraManager getCameraManager() {
		return cameraManager;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		}
		
		setContentView(R.layout.capture);
		
		mBtnBack = (Button) findViewById(R.id.button);
		mBtnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (lastResult != null) {
					restartPreviewAfterDelay(0L);
				} else {
					finish();
				} 
			}
		});
		
		
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		cameraManager = new CameraManager(this);
		
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);
		mBtnLight = (CheckBox) findViewById(R.id.button2);
		mBtnLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if (b) {
					cameraManager.setTorch(true);
				} else {
					cameraManager.setTorch(false);
				}
				
			}
		});
		resultView = findViewById(R.id.result_view);
		statusView = (TextView) findViewById(R.id.status_view);
		
		handler = null;
		lastResult = null;
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		resetStatusView();
		
		ambientLightManager.start(cameraManager);
		
		inactivityTimer.onResume();
		
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the camera.
			surfaceHolder.addCallback(this);
		}
		
		mBtnGallery = (Button) findViewById(R.id.button3);
		mBtnGallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openGallery();
			}
		});
	}
	
	/**
	 * 打开相册
	 */
	private void openGallery() {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(pickIntent, 101);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK && requestCode == 101) {
			Uri uri = data.getData();
			final String path = uri2path(uri);
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(path);
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					final String s = scanQRImage(bitmap);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							statusView.setVisibility(View.GONE);
							viewfinderView.setVisibility(View.GONE);
							resultView.setVisibility(View.VISIBLE);
							
							TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
							contentsTextView.setText(s);
						}
					});
					
					System.out.println(s);
				}
			}).start();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		beepManager.close();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				
				if (lastResult != null) {
					restartPreviewAfterDelay(0L);
					return true;
				}
				break;
			case KeyEvent.KEYCODE_FOCUS:
			case KeyEvent.KEYCODE_CAMERA:
				// Handle these events so they don't launch the Camera app
				return true;
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	/**
	 * A valid barcode has been found, so give an indication of success and show the results.
	 *
	 * @param rawResult   The contents of the barcode.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param barcode     A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
		
		boolean fromLiveScan = barcode != null;
		if (fromLiveScan) {
			beepManager.playBeepSoundAndVibrate();
			drawResultPoints(barcode, scaleFactor, rawResult);
		}
		
		handleDecodeInternally(rawResult, resultHandler, barcode);
		
	}
	
	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
	 *
	 * @param barcode     A bitmap of the captured image.
	 * @param scaleFactor amount by which thumbnail was scaled
	 * @param rawResult   The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
			} else if (points.length == 4 &&
					(rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
							rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and metadata
				drawLine(canvas, paint, points[0], points[1], scaleFactor);
				drawLine(canvas, paint, points[2], points[3], scaleFactor);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					if (point != null) {
						canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
					}
				}
			}
		}
	}
	
	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
		if (a != null && b != null) {
			canvas.drawLine(scaleFactor * a.getX(),
					scaleFactor * a.getY(),
					scaleFactor * b.getX(),
					scaleFactor * b.getY(),
					paint);
		}
	}
	
	// Put up our own UI for how to handle the decoded contents.
	private void handleDecodeInternally(Result rawResult, ResultHandler resultHandler, Bitmap barcode) {
		
		CharSequence displayContents = resultHandler.getDisplayContents();
		
		statusView.setVisibility(View.GONE);
		viewfinderView.setVisibility(View.GONE);
		resultView.setVisibility(View.VISIBLE);
		
		TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
		contentsTextView.setText(displayContents);
		System.out.println("二维码信息:" + displayContents);
		
	}
	
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, null, null, null, cameraManager);
			}
			
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}
	
	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}
	
	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}
	
	private void resetStatusView() {
		resultView.setVisibility(View.GONE);
		statusView.setText(R.string.msg_default_status);
		statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}
	
	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
	
	
	public String scanQRImage(Bitmap bMap) {
		String contents = null;
		
		int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
		//copy pixel data from the Bitmap into the 'intArray' array
		bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
		
		LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		
		Reader reader = new MultiFormatReader();
		try {
			Result result = reader.decode(bitmap);
			lastResult = result;
			contents = result.getText();
		} catch (Exception e) {
			Log.e("QrTest", "Error decoding barcode", e);
		}
		return contents;
	}
	
	/**
	 * Uri 转 path
	 * 
	 * @param uri
	 * @return
	 */
	public String uri2path(Uri uri) {
		String path = "";
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			path = cursor.getString(column_index);
			cursor.close();
		}
		return path;
	}
}
