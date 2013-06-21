package com.example.weibonju;

import gyx.weibosdk.Constants;

import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibonju.action.IAccountMatter;
import com.weibonju.action.impl.AccountMatter;
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
	static final int DELAY=3000;
	long start;
	private Weibo mWeibo;
	private IAccountMatter acc;
	
	private SsoHandler mSsoHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		acc=new AccountMatter(this,(WeiboContext)getApplication());
		mWeibo = Weibo.getInstance(Constants.APP_KEY, Constants.REDIRECT_URL,Constants.SCOPE);
		//确认是否已经登录,登录是否过期
		acc.getTokenFromSP();
		
		/*SSO认证
		findViewById(R.id.sso).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler = new SsoHandler(WelcomeActivity.this, mWeibo);
                mSsoHandler.authorize(new AuthDialogListener(),null);
            }
        });
        */
		
		Button login=(Button)this.findViewById(R.id.loginButton);
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mWeibo.anthorize(WelcomeActivity.this, new AuthDialogListener());
			}
		});
		
		if (acc.isLogin()) {
			//登录OK
			acc.ContextSetToken();
			PassToMain();
		}else{
			login.setVisibility(View.VISIBLE);
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
			if(code==null){
				Toast.makeText(getApplicationContext(),
		                "网络连接有问题", Toast.LENGTH_LONG).show();
				return;
			}
			
			System.out.println("2");
			
			new Thread(new Runnable(){
				@Override
				public void run() {
					if(acc.getTokenFromSina(code)){
				        Message msg = new Message();
				        handler.sendMessage(msg);
					}
					else{
						Toast.makeText(getApplicationContext(),
				                "网络连接有问题", Toast.LENGTH_LONG).show();
					}
				}
			}).start();
			
	
		}
		
		

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
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
	        PassToMain();
		}
		
	};
    
	private void PassToMain() {
		start=System.currentTimeMillis();
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
