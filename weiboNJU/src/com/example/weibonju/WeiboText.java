package com.example.weibonju;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class WeiboText extends LinearLayout{

	public WeiboText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public WeiboText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WeiboText(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.weibo_text, this);
	}

}
