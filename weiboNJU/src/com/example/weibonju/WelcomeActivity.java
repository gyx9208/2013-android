package com.example.weibonju;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibonju.configure.AccessTokenKeeper;
import com.weibonju.configure.Constants;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.weibonju.configure.WeiboContext;

public class WelcomeActivity extends Activity {

	static final int DELAY=1000;
	long start;
	
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	
	private SsoHandler mSsoHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		//start=System.currentTimeMillis();
		mWeibo = Weibo.getInstance(Constants.APP_KEY, Constants.REDIRECT_URL,Constants.SCOPE);
		//确认是否已经登录,登录是否过期
		accessToken=AccessTokenKeeper.readAccessToken(this);
		
		findViewById(R.id.sso).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler = new SsoHandler(WelcomeActivity.this, mWeibo);
                mSsoHandler.authorize(new AuthDialogListener(),null);
            }
        });
		Button login=(Button)this.findViewById(R.id.loginButton);
		login.setVisibility(View.VISIBLE);
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mWeibo.anthorize(WelcomeActivity.this, new AuthDialogListener());
			}
		});
		
		if (accessToken.isSessionValid()) {
			//登录OK
			Toast.makeText(getApplicationContext(),"在prefer里找到合法token", Toast.LENGTH_SHORT).show();
		}
	}
	
	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
	                Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Bundle values) {
			System.out.println("1");
			final String code = values.getString("code");
			if(code!=null){
				Toast.makeText(WelcomeActivity.this, "认证code成功", Toast.LENGTH_SHORT).show();
			}
			
			System.out.println("2");
			new Thread(new Runnable(){
				public void run() {
					// TODO Auto-generated method stub
					try{
						URL url = new URL("https://api.weibo.com/oauth2/access_token");
						URLConnection connection = url.openConnection();
						connection.setDoOutput(true);
						OutputStreamWriter out = new OutputStreamWriter(connection  
				                .getOutputStream(), "utf-8");
						out.write("client_id="+Constants.APP_KEY+"&client_secret="+Constants.APP_SECRET+"&grant_type=authorization_code" +
								"&code="+code+"&redirect_uri="+Constants.REDIRECT_URL);
						out.flush();  
				        out.close();
				        System.out.println("3");
				        String sCurrentLine;  
				        String sTotalString;  
				        sCurrentLine = "";  
				        sTotalString = "";  
				        InputStream l_urlStream;  
				        l_urlStream = connection.getInputStream();  
				        BufferedReader l_reader = new BufferedReader(new InputStreamReader(  
				                l_urlStream));  
				        while ((sCurrentLine = l_reader.readLine()) != null) {  
				            sTotalString += sCurrentLine;  
				        }
				        System.out.println("4");
				        JSONObject o=new JSONObject(sTotalString);
				        String access_token=o.getString("access_token");
				        String expires_in=o.getString("expires_in");
				        String uid=o.getString("uid");
				        System.out.println(access_token+" "+expires_in);
				        
				        Message msg = new Message();
				        Bundle b = new Bundle();
				        b.putString("access_token", access_token);
				        b.putString("expires_in", expires_in);
				        b.putString("uid", uid);
				        msg.setData(b);
				        handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
			}).start();
		}
		
		private Handler handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle b = msg.getData();
				String access_token=b.getString("access_token");
		        String expires_in=b.getString("expires_in");
		        String uid=b.getString("uid");
		        Oauth2AccessToken accessToken = new Oauth2AccessToken(access_token, expires_in);
		        ((WeiboContext)getApplication()).setAccessToken(accessToken);
		        Toast.makeText(getApplicationContext(), access_token+" "+expires_in, Toast.LENGTH_SHORT).show();
			}
			
		};

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
	                "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
	                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
	                .show();
		}

	}
	
	
    
	private void PassToMain() {
		((WeiboContext)getApplication()).setAccessToken(accessToken);
		new Thread(new Runnable(){
			public void run(){
				Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
				long end=System.currentTimeMillis();
				if(end-start<DELAY){
					try{
						Thread.sleep(DELAY-(end-start));
					} catch (InterruptedException e){
						e.printStackTrace();
					}
				}
				startActivity(intent);
				finish();
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // sso 授权回调
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
