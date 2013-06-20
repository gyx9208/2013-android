package com.example.weibonju;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableRow;

public class CampusRefreshRow extends TableRow {

	public CampusRefreshRow(Context context) {
		super(context);
		init(context);
	}

	public CampusRefreshRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context){
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.campus_refresh_row, this);
	}
}
