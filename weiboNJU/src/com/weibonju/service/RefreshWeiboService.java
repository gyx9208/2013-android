package com.weibonju.service;

import com.example.weibonju.MainActivity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class RefreshWeiboService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		new Thread(new Runnable() {  
            public void run() {  
                Log.d("mark", "Service onCreate: " + "\n" + "当前线程名称："  
                        + Thread.currentThread().getName() + "," + "当前线程id："  
                        + Thread.currentThread().getId());  
            }  
        }).start();  
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		new Thread(new Runnable() {  
            public void run() {  
                Log.d("mark", "Service onDestroy: " + "\n" + "当前线程名称："  
                        + Thread.currentThread().getName() + "," + "当前线程id："  
                        + Thread.currentThread().getId());  
            }  
        }).start();  
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {  
            public void run() {  
                Log.d("mark", "Service onStart: " + "\n" + "当前线程名称："  
                        + Thread.currentThread().getName() + "," + "当前线程id："  
                        + Thread.currentThread().getId());  
                try {
        			Thread.sleep(5000);
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
        		MainActivity.handler.sendMessage(new Message());
        		
            }  
        }).start();  
		return super.onStartCommand(intent, flags, startId);
	}


}
