package com.weibonju.action.impl;

import gyx.weibosdk.AccessTokenAPI;
import android.content.Context;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibonju.action.IAccountMatter;
import com.weibonju.configure.AccessTokenKeeper;
import com.weibonju.configure.WeiboContext;

public class AccountMatter implements IAccountMatter {
	private Context context;
	private WeiboContext weiboContext;
	private Oauth2AccessToken token;
	
	public AccountMatter(Context p1, WeiboContext p2){
		context=p1;
		weiboContext=p2;
	}

	@Override
	public boolean isLogin() {
		return token.isSessionValid();
	}

	@Override
	public void setToken(Oauth2AccessToken token) {
		this.token=token;
	}

	@Override
	public void SPSetToken() {
		AccessTokenKeeper.keepAccessToken(context, token);
	}

	@Override
	public void ContextSetToken() {
		weiboContext.setAccessToken(token);
	}

	@Override
	public Oauth2AccessToken getToken() {
		return token;
	}

	@Override
	public boolean getTokenFromSina(String code) {
		AccessTokenAPI action=new AccessTokenAPI();
		try{
			token=action.getTokenFromSina(code);
			SPSetToken();
			ContextSetToken();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void getTokenFromSP() {
		token=AccessTokenKeeper.readAccessToken(context);
	}

	@Override
	public void logout() {
		AccessTokenKeeper.clear(context);
		weiboContext.clearAccessToken();
	}

}
