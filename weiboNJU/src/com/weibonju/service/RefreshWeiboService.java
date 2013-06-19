package com.weibonju.service;

import com.example.weibonju.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class RefreshWeiboService extends Service {

	Thread downloadThread;
	Thread timerThread;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("mark", "Service onCreate: " + "\n" + "当前线程名称："  
                + Thread.currentThread().getName() + "," + "当前线程id："  
                + Thread.currentThread().getId()); 
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("mark", "Service onDestroy: " + "\n" + "当前线程名称："  
                + Thread.currentThread().getName() + "," + "当前线程id："  
                + Thread.currentThread().getId());  
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("mark", "Service onStart: " + "\n" + "当前线程名称："  
                + Thread.currentThread().getName() + "," + "当前线程id："  
                + Thread.currentThread().getId());
		
		
		
		new Thread(new Runnable() {  
            public void run() {   
            	try {
        			Thread.sleep(10000);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		MainActivity.handler.sendMessage(new Message());
                Looper.prepare();  
                Toast.makeText(RefreshWeiboService.this, "网络连接缓慢，请稍后再试", Toast.LENGTH_SHORT).show();  
                Looper.loop();  
            }  
        }).start();
		return START_NOT_STICKY;
	}


}
