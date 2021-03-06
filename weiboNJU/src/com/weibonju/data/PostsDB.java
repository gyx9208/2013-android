package com.weibonju.data;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PostsDB {
	private DBHelper dbHelper;
	private final static String TABLE_NAME =DBHelper.TABLE_NAME;
	
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SQLiteDatabase database;
	public PostsDB(Context context){
		dbHelper=new DBHelper(context);
		
	}
	
	public void saveAll(ArrayList<SinglePost> posts){
		database = dbHelper.getWritableDatabase();
		database.execSQL("DELETE FROM "+ TABLE_NAME);
		for(SinglePost post:posts){
			ContentValues contentValues = new ContentValues();
			contentValues.put("pid", post.getPid());
			contentValues.put("created_at", format.format(post.getCreated_at()));
			contentValues.put("text", post.getText());
			contentValues.put("reposts_count", post.getReposts_count());
			contentValues.put("comments_count", post.getComments_count());
			contentValues.put("attitudes_count", post.getAttitudes_count());
			contentValues.put("pic_ids", post.getPic_ids());
			contentValues.put("source", post.getSource());
			contentValues.put("uid", post.getUid());
			contentValues.put("screen_name", post.getScreen_name());
			contentValues.put("profile_image_url", post.getProfile_image_url().toString());
			contentValues.put("gender", post.getGender());
			database.insert(TABLE_NAME, null, contentValues);
		}
		database.close();
	}
	
	public void clear(){
		database = dbHelper.getWritableDatabase();
		database.execSQL("DELETE FROM "+ TABLE_NAME);
		database.close();  
	}
	
	public ArrayList<SinglePost> getALL(){
		database = dbHelper.getWritableDatabase();
		ArrayList<SinglePost> posts=new ArrayList<SinglePost>();
		Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_NAME + " ORDER BY pid DESC", null); 
		while (c.moveToNext()) {  
            SinglePost post = new SinglePost();  
            post.setPid(c.getLong(c.getColumnIndex("pid")));
            post.setText(c.getString(c.getColumnIndex("text")));
            post.setReposts_count(c.getInt(c.getColumnIndex("reposts_count")));
            post.setComments_count(c.getInt(c.getColumnIndex("comments_count")));
            post.setAttitudes_count(c.getInt(c.getColumnIndex("attitudes_count")));
            post.setPic_ids(c.getString(c.getColumnIndex("pic_ids")));
            post.setSource(c.getString(c.getColumnIndex("source")));
            post.setUid(c.getLong(c.getColumnIndex("uid")));
            post.setScreen_name(c.getString(c.getColumnIndex("screen_name")));
            post.setGender(c.getString(c.getColumnIndex("gender")));
            try {
            	post.setProfile_image_url(new URL(c.getString(c.getColumnIndex("profile_image_url"))));
				post.setCreated_at(format.parse(c.getString(c.getColumnIndex("created_at"))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            posts.add(post);
        }  
		database.close();  
		return posts;
	}
	
	
	public long getCount() {  
        SQLiteDatabase database = dbHelper.getReadableDatabase();  
        Cursor cursor = database.query(TABLE_NAME, new String[] { "count(*)" },  
                null, null, null, null, null);  
        if (cursor.moveToNext()) {  
            return cursor.getLong(0);  
        }  
        return 0;  
    }

}
