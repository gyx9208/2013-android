package gyx.weibosdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weibonju.data.SinglePost;

public class PoiTimelineAPI {
	
	public ArrayList<SinglePost> getList(String token, String poi, int num) throws IOException, JSONException{
		URL url = new URL("https://api.weibo.com/2/place/poi_timeline.json"+"?poiid="+poi+"&count="+num+"&access_token="+token);
		return getList(url);
	}
	
	public ArrayList<SinglePost> getList(String token, String poi, int num,
			long pid) throws IOException, JSONException{
		URL url = new URL("https://api.weibo.com/2/place/poi_timeline.json"+"?poiid="+poi+"&count="+num+"&max_id="+pid+"&access_token="+token);
		return getList(url);
	}
	
	private ArrayList<SinglePost> getList(URL url) throws IOException, JSONException{
		URLConnection connection = url.openConnection();
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
        JSONObject o=new JSONObject(sTotalString);
        ArrayList<SinglePost> list=analyse(o);
		return list;
	}

	private ArrayList<SinglePost> analyse(JSONObject o) throws JSONException {
		ArrayList<SinglePost> list=new ArrayList<SinglePost>();
		JSONArray statuses=o.getJSONArray("statuses");
		SimpleDateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		for(int i=0;i<statuses.length();i++){
			SinglePost post=new SinglePost();
			JSONObject p=statuses.getJSONObject(i);
			Date create;
			try {
				create=format.parse(p.getString("created_at"));
				post.setCreated_at(create);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			post.setUid(p.getLong("id"));
			post.setText(p.getString("text"));
			post.setReposts_count(p.getInt("reposts_count"));
			post.setComments_count(p.getInt("comments_count"));
			post.setAttitudes_count(p.getInt("attitudes_count"));
			JSONArray Jpic_ids=p.getJSONArray("pic_ids");
			StringBuilder pics=new StringBuilder();
			for(int j=0;j<Jpic_ids.length();j++){
				if(j==0)
					pics.append(Jpic_ids.getString(j));
				else
					pics.append(",").append(Jpic_ids.getString(j));
			}
			post.setPic_ids(pics.toString());
			String source=p.getString("source");
			int x=source.indexOf('>');
			int y=source.indexOf('<', 1);
			post.setSource(source.substring(x+1, y));
			
			JSONObject u=p.getJSONObject("user");
			post.setUid(u.getLong("id"));
			post.setScreen_name(u.getString("screen_name"));
			post.setGender(u.getString("gender"));
			try {
				post.setProfile_image_url(new URL(u.getString("profile_image_url")));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			list.add(post);
		}
		
		return list;
	}

	
}
