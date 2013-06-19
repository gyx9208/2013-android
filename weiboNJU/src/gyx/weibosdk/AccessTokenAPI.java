package gyx.weibosdk;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.weibo.sdk.android.Oauth2AccessToken;

public class AccessTokenAPI {

	public AccessTokenAPI() {
		
	}
	
	public Oauth2AccessToken getTokenFromSina(String code) throws Exception{
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
        
        System.out.println(access_token+" "+expires_in);
        Oauth2AccessToken token =new Oauth2AccessToken(access_token, expires_in);
		return token;
	}
	
}
