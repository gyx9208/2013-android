package com.weibonju.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public class AsyncImageLoader {
	@SuppressLint("SdCardPath")
	private static final String FILEDIR="/data/data/com.example.weibonju/files";
	
	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	private final Handler handler=new Handler();
	private final Resources res;
	
	public AsyncImageLoader(Resources res){
		this.res=res;
	}
	
	public void shutDown(){
		executorService.shutdown();
	}
	
	public Drawable loadDrawable(final boolean ifNet,final String imageUrl,final String fileName, final ImageCallback callback) {
        //如果存储里有就从Internal Storage中取出数据
		final File f=new File(FILEDIR,fileName);
        if (f.exists()) {
            return Drawable.createFromPath(f.getAbsolutePath());
        }
        //内存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
        if(ifNet) 
        	executorService.submit(new Runnable() {
            public void run() {
                try {
                	URL imgURL=new URL(imageUrl);
                	URLConnection conn  = imgURL.openConnection();
                	conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream); 
                    inputStream.close();
                    
                    FileOutputStream fos=new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    
                    final Drawable drawable=new BitmapDrawable(res,bitmap);
                    
                    handler.post(new Runnable() {
                        public void run() {
                           callback.imageLoaded(drawable);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return null;
    }
	
	public interface ImageCallback {
        //注意 此方法是用来设置目标对象的图像资源
        public void imageLoaded(Drawable imageDrawable);
    }
}
