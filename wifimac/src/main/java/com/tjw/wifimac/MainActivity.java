package com.tjw.wifimac;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	
	private TextView mTextView;
	private TextView mTextView1;
	private WifiManager mWifiManager;
	private List<ScanResult> mSameSSID;
	private WifiInfo mInfo;
	private String mBSSID;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextView = (TextView) findViewById(R.id.textView);
		mTextView1 = (TextView) findViewById(R.id.textView1);
		
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		List<ScanResult> scanResults = mWifiManager.getScanResults();
		
		mInfo = mWifiManager.getConnectionInfo();
		String currentSSID = mInfo.getSSID();
		
		mTextView.setText(currentSSID);
		
		mBSSID = mInfo.getBSSID();
		mTextView1.setText("路由器mac --> "+ mBSSID);

		mSameSSID = new ArrayList<>();
		for (ScanResult s : scanResults) {
			System.out.println(s.SSID);
			if (currentSSID.equals("\"" + s.SSID + "\"")) {
				mSameSSID.add(s);
			}
		}
		
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				System.out.println(mBSSID);
//				checkWifi();
				if (!mBSSID.equals(mWifiManager.getConnectionInfo().getBSSID())) {
					notiiii();
					mBSSID = mWifiManager.getConnectionInfo().getBSSID(); 
				}
				
				mTextView1.setText("路由器mac --> "+mWifiManager.getConnectionInfo().getBSSID());
				handler.postDelayed(this, 1000L);
			}
		}, 1000L);
	}
	
	private void checkWifi() {
		if (mSameSSID.size() > 2) {
			int maxlevel = mSameSSID.get(0).level;
			String bssid = mSameSSID.get(0).BSSID;
			
			for (ScanResult s : mSameSSID) {
				mTextView1.setText("路由器mac --> "+mInfo.getBSSID());
				System.out.println("maxlevel"+maxlevel);
				System.out.println("s"+s.level);
//				if (s.level > maxlevel) {
//					mTextView1.setText("路由器mac --> "+s.BSSID);
////					notiiii();
////					mWifiManager.disconnect();
////					mWifiManager.reconnect();
//					mBSSID = s.BSSID;
//				}
				if (!s.BSSID.equals(mBSSID)) {
					
				}
			}
		}
	}
	
	public void getWifiInfo(View view) {
		
		WifiInfo info = mWifiManager.getConnectionInfo();
		String currentSSID = info.getSSID();
		
		String maxText = info.getMacAddress();
		String ipText = intToIp(info.getIpAddress());
		String status = "";
		
		if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
			status = "WIFI_STATE_ENABLED";
		}
		String ssid = info.getSSID();
		String bssid = info.getBSSID();
		int networkID = info.getNetworkId();
		int speed = info.getLinkSpeed();
		int infoRssi = info.getRssi();
		
		
		String infos = "本机mac：" + maxText + "\n\r"
				+ "路由器mac :" + bssid + "\n\r"
				+ "ip：" + ipText + "\n\r"
				+ "wifi status :" + status + "\n\r"
				+ "ssid :" + ssid + "\n\r"
				+ "net work id :" + networkID + "\n\r"
				+ "connection speed:" + speed + "\n\r"
				+ "Rssi:" + infoRssi + "\n\r";
		mTextView1.setText(infos);
		
	}
	
	private String intToIp(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 24) & 0xFF);
	}
	
	public void clear(View view) {
		mTextView1.setText("");
	}
	
	public void notify(View view) {
		notiiii();
	}
	
	private void notiiii() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle("WIFI状态已改变mac : "+mWifiManager.getConnectionInfo().getBSSID())//设置通知栏标题  
				.setContentText("WIFI")
				.setTicker("测试WIFI") //通知首次出现在通知栏，带上升动画效果的  
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
				.setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级  
//  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消    
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
				.setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
				//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission  
				.setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON  
		mNotificationManager.notify(0,mBuilder.build());
	}
}
