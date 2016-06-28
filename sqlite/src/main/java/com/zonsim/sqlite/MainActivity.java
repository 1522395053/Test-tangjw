package com.zonsim.sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zonsim.sqlite.bean.JobsBean;
import com.zonsim.sqlite.dao.JobDao;
import com.zonsim.sqlite.helper.MyDataBaseHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
	
	private JobDao mJobDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyDataBaseHelper helper = new MyDataBaseHelper(this);
		
		helper.getWritableDatabase();
		
		mJobDao = new JobDao(this);
		getJobs();
		
	}
	
	/**
	 * 获取工种列表
	 */
	private void getJobs() {
		new Thread() {
			@Override
			public void run() {
				try {
					URL url = new URL("http://192.168.1.233:8080/android/jobs.json");
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					
					if (connection.getResponseCode() == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = stream2string(inputStream);
						/*ArrayList<JobsBean> jobList = new Gson().fromJson(result, new TypeToken<ArrayList<JobsBean>>() {
						}.getType());*/
						
						JobsBean jobsBean = new Gson().fromJson(result, JobsBean.class);
						
						if (jobsBean.isIsUpdate()) {
							mJobDao.delete();
							for (JobsBean.JobBean job : jobsBean.getJobs()) {
								long insert = mJobDao.insert(job.getProfessionCode(), job.getProfessionName(), job.getId());
								Logger.i(job.getProfessionName());
								Logger.i(insert + "");
							}
						}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	private String stream2string(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1; ) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}
