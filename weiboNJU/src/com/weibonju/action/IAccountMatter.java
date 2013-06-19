package com.weibonju.action;

import com.weibo.sdk.android.Oauth2AccessToken;

public interface IAccountMatter {
	public boolean isLogin();
	public void setToken(Oauth2AccessToken token);
	public void SPSetToken();
	public void ContextSetToken();
	public Oauth2AccessToken getToken();
	public boolean getTokenFromSina(String code);
	public void getTokenFromSP();
	public void logout();
	
}
