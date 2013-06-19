package com.weibonju.configure;

import gyx.weibosdk.Constants;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import android.app.Application;


public class WeiboContext extends Application {
	
	private Oauth2AccessToken accessToken;
    private Weibo mWeibo;
    private String uid;
    
    
    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public WeiboContext() {
		super();
		mWeibo = Weibo.getInstance(Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
	}
    
	public Oauth2AccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(Oauth2AccessToken accessToken) {
		this.accessToken = accessToken;
	}
	public void clearAccessToken(){
		this.accessToken = new Oauth2AccessToken();
	}
	
	public Weibo getmWeibo() {
		return mWeibo;
	}
	public void setmWeibo(Weibo mWeibo) {
		this.mWeibo = mWeibo;
	}
	

}
