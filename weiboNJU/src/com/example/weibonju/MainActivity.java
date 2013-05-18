package com.example.weibonju;

import java.util.ArrayList;
import java.util.List;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ViewPager viewPager;
	private ImageView imageView;
	private TextView textView1,textView2,textView3,textView4;
	private List<View> views;
	private int offset = 0;
	private int currIndex = 0;
	private int viewNum=4;
	private int bmpW;
	private View view1,view2,view3,view4;
	

    public static final String TAG = "weibo-gyx";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitImageView();
        InitTextView();
        InitViewPager();
        InitSpinner();
    }

	private void InitSpinner() {
		Spinner spinner=(Spinner)view1.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.positiontext, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	class SpinnerXMLSelectedListener implements OnItemSelectedListener{  
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {  
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
              
        }  
          
    }

	private void InitViewPager() {
		viewPager=(ViewPager) findViewById(R.id.vPager);
        views=new ArrayList<View>();
        LayoutInflater inflater=getLayoutInflater();
        view1=inflater.inflate(R.layout.campus, null);
        view2=inflater.inflate(R.layout.message, null);
        view3=inflater.inflate(R.layout.friend, null);
        view4=inflater.inflate(R.layout.setting, null);
        views.add(view1);  
        views.add(view2);  
        views.add(view3);  
        views.add(view4); 
        viewPager.setAdapter(new MyViewPagerAdapter(views));  
        viewPager.setCurrentItem(0);  
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener()); 
	}
	
	private void InitTextView() {
		// TODO Auto-generated method stub
		textView1 = (TextView) findViewById(R.id.text1);  
        textView2 = (TextView) findViewById(R.id.text2);  
        textView3 = (TextView) findViewById(R.id.text3);  
        textView4 = (TextView) findViewById(R.id.text4); 
        textView1.setOnClickListener(new MyOnClickListener(0));  
        textView2.setOnClickListener(new MyOnClickListener(1));  
        textView3.setOnClickListener(new MyOnClickListener(2));
        textView4.setOnClickListener(new MyOnClickListener(3));
	}

	private void InitImageView() {
		imageView= (ImageView) findViewById(R.id.cursor);  
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.bar).getWidth();// ��ȡͼƬ���  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;
        offset = (screenW / viewNum - bmpW) / 2;
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
        imageView.setImageMatrix(matrix);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	private class MyOnClickListener implements OnClickListener{  
        private int index=0;  
        public MyOnClickListener(int i){  
            index=i;  
        }  
        public void onClick(View v) {  
            viewPager.setCurrentItem(index);              
        }  
    }  
    
	public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
          
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        }  
  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position));  
        }  
  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {            
             container.addView(mListViews.get(position), 0);  
             return mListViews.get(position);  
        }  
  
        @Override  
        public int getCount() {           
            return  mListViews.size();  
        }  
          
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1;  
        }  
	}
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener{  
    	int one = offset * 2 + bmpW;
    	public void onPageScrollStateChanged(int arg0) {  
        }  
  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
  
        public void onPageSelected(int arg0) {  
        	Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);//��Ȼ����Ƚϼ�ֻ࣬��һ�д��롣  
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(50);
            imageView.startAnimation(animation);
        }  
    }
}
