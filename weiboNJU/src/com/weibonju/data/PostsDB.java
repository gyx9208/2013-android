package com.weibonju.data;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
		database = dbHelper.getWritableDatabase();
	}
	
	public void save(SinglePost post){
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
		database.insert(TABLE_NAME, null, contentValues);
	}
	
	public void clear(){
		database.execSQL("DELETE FROM "+ TABLE_NAME);
	}
	
	public List<SinglePost> getALL(){
		ArrayList<SinglePost> posts=new ArrayList<SinglePost>();
		Cursor c = database.rawQuery("SELECT * FROM "+ TABLE_NAME, null); 
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
            try {
            	post.setProfile_image_url(new URL(c.getString(c.getColumnIndex("profile_image_url"))));
				post.setCreated_at(format.parse(c.getString(c.getColumnIndex("created_at"))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            posts.add(post);
        }  
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
	
	public void closeDB() {  
        database.close();  
    }

}
