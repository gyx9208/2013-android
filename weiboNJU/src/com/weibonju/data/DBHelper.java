package com.weibonju.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "weibonju.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "weiboposts";
    private static final String DATABASE_CREATE=
    		"CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
    		" (pid INTEGER PRIMARY KEY, created_at DATETIME, " +
    		"text NVARCHAR, reposts_count INTEGER, comments_count INTEGER, attitudes_count INTEGER, " +
    		"pic_ids VARCHAR, source NVARCHAR, uid INTEGER, " +
    		"screen_name NVARCHAR, profile_image_url VARCHAR, gender VARCHAR)";
    private static final String DATABASE_DELETE=
    		"DROP TABLE IF EXISTS weiboposts";
    
    
	public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_DELETE);  
        onCreate(db);
	}

}
