package com.weibonju.service;

import gyx.weibosdk.PoiTimelineAPI;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.weibonju.data.SinglePost;

public class AppendAsyncTask extends AsyncTask<String, java.lang.Void, ArrayList<SinglePost>> {
	private static AppendAsyncTask instance=null;
	private int NUM=20;
	private int WAITETIME=10000;
	private Handler handler;
	
	public static AppendAsyncTask getInstance(Handler handler){
		if(instance==null){
			instance=new AppendAsyncTask(handler);
		}
		return instance;
	}
	
	public static void ReleaseInstance(){
		instance=null;
	}
	
	private AppendAsyncTask() {
		super();
	}
	
	private AppendAsyncTask(Handler handler) {
		super();
		this.handler = handler;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	Thread timerThread;
	
	@Override
	protected ArrayList<SinglePost> doInBackground(String... params) {
		timerThread=new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(WAITETIME);
					Message m=new Message();
					m.what=0;
					handler.sendMessage(m);
				} catch (InterruptedException e) {
				} 
			}
		});
		timerThread.start();
		String token=params[0];
		String poi=params[1];
		String spid=params[2];
		long pid=Long.parseLong(spid);
		PoiTimelineAPI api=new PoiTimelineAPI();
		ArrayList<SinglePost> list=null;
		try {
			list = api.getList(token, poi, NUM, pid);
		} catch (Exception e) {
			e.printStackTrace();
			list=null;
		}
		return list;
	}
	
	/**
	 * doInBackground执行完以后执行这段
	 */
	@Override
	protected void onPostExecute(ArrayList<SinglePost> result) {
		timerThread.interrupt();
		if(result==null){
			Message m=new Message();
			m.what=2;
			handler.sendMessage(m);
		}else{
			Message m=new Message();
			m.what=1;
			Bundle b=new Bundle();
			b.putSerializable("list",result);
			m.setData(b);
			handler.sendMessage(m);
		}
	}
	
	/**
	 * 如果被取消了，执行这个
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
		timerThread.interrupt();
	}
}
