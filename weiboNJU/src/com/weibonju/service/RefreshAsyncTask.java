package com.weibonju.service;

import java.util.ArrayList;
import com.weibonju.data.SinglePost;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 单例模式，防止多次刷新
 * @author gyx
 *
 */
public class RefreshAsyncTask extends AsyncTask<String, java.lang.Void, ArrayList<SinglePost>> {

	private static RefreshAsyncTask instance=null;
	private static final int NUM=20;
	private Handler handler;
	
	/**
	 * 务必在获得实例后检查是否正在运行
	 * task != null && task.getStatus() == AsyncTask.Status.RUNNING
	 */
	public static RefreshAsyncTask getInstance(Handler handler){
		if(instance==null){
			instance=new RefreshAsyncTask(handler);
		}
		return instance;
	}
	
	/**
	 * 务必在执行成功或者cancel以后才release
	 */
	public static void ReleaseInstance(){
		instance=null;
	}
	
	private RefreshAsyncTask() {
		super();
	}
	
	private RefreshAsyncTask(Handler handler) {
		super();
		this.handler = handler;
	}
	
	/**
	 * 预先执行的内容，对UI更新，调出refresh动画
	 */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	/**
	 * 预执行完毕后，下载内容
	 */
	@Override
	protected ArrayList<SinglePost> doInBackground(String... params) {
		
		return null;
	}

	/**
	 * doInBackground执行完以后执行这段
	 */
	@Override
	protected void onPostExecute(ArrayList<SinglePost> result) {
		Message m=new Message();
		m.what=1;
		Bundle b=new Bundle();
		b.putSerializable("list",result);
		m.setData(b);
		handler.sendMessage(m);
	}
	
	/**
	 * 如果被取消了，执行这个
	 */
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}
	
}
