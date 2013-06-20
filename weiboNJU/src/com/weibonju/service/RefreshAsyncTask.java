package com.weibonju.service;

import java.util.List;
import com.weibonju.data.SinglePost;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * 单例模式，防止多次刷新
 * @author gyx
 *
 */
public class RefreshAsyncTask extends AsyncTask<java.lang.Void, java.lang.Void, List<SinglePost>> {
	
	

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
	protected List<SinglePost> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * doInBackground执行完以后执行这段
	 */
	@Override
	protected void onPostExecute(List<SinglePost> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
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
