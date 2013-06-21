package com.example.weibonju;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.weibonju.action.IAccountMatter;
import com.weibonju.action.impl.AccountMatter;
import com.weibonju.configure.ConfigureKeeper;
import com.weibonju.configure.WeiboContext;
import com.weibonju.data.PostsDB;
import com.weibonju.data.SinglePost;
import com.weibonju.service.AppendAsyncTask;
import com.weibonju.service.AsyncImageLoader;
import com.weibonju.service.RefreshAsyncTask;

/**
 * 主界面
 * @author gyx
 *
 */
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
	private ArrayList<SinglePost> nowList;
    public static final String TAG = "weibo-gyx";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitImageView();
        InitTextView();
        InitViewPager();
        InitSpinner();
        InitRefreshFunction();
        LoadInfo();
    }

	/**
     *  加载之前浏览的页面，加载数据库中的内容
     */
	private void LoadInfo() {
		Spinner spinner=(Spinner)view1.findViewById(R.id.spinner1);
		spinner.setSelection(ConfigureKeeper.readSpinnerPos(this));
		PostsDB db=new PostsDB(this);
		nowList=db.getALL();
		for(int i=0;i<nowList.size();i++){
			SinglePost p=nowList.get(i);
			insertPost(p,i*2,false);
		}
	}

	/**
	 * 初始化下拉框
	 */
	private void InitSpinner() {
		Spinner spinner=(Spinner)view1.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.positiontext, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		int start=0;
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,  
                long arg3) {
        	if(start==0){
        		start=1;
        		return;
        	}
        	finishAction();
        	StartRefreshAction();
        }  
  
        public void onNothingSelected(AdapterView<?> arg0) {  
              
        }  
          
    }

	/**
	 * 初始化页面
	 */
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


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId())
        {
        case R.id.menu_logout:
        	logout();
            break;
        case R.id.menu_clear:
        	clearInternalStorage();
        	break;
        }
		return true;
	}

	private void clearInternalStorage() {
		Toast.makeText(this, "头像、图片和缓存已经清空", Toast.LENGTH_SHORT).show();
		finishAction();
		TableLayout t=(TableLayout)view1.findViewById(R.id.MainTable);
		int length=t.getChildCount();
		for(int i=0;i<length-1;i++){
			t.removeViewAt(0);
		}
		nowList.clear();
		PostsDB db=new PostsDB(this);
		db.clear();
		for(String fn:this.fileList()){
			this.deleteFile(fn);
		}	
	}

	private void logout() {
		finishAction();
		nowList.clear();
		saveConfigure();
		IAccountMatter acc=new AccountMatter(this,(WeiboContext)getApplication());
		acc.logout();
		Intent intent=new Intent(MainActivity.this,WelcomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	private Handler refreshHandler;
	private Handler appendHandler;
	private AsyncImageLoader imgLoader;
	
	@SuppressLint("HandlerLeak")
	private void InitRefreshFunction() {
		//refresh刷新
		refreshHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0://网络太慢，10秒内没有接到数据
					if(refreshTask!=null){
						refreshTask.cancel(true);
						RemoveRefreshViews();
						Toast.makeText(MainActivity.this, "网络连接缓慢，请稍后再试", Toast.LENGTH_SHORT).show();  
					}break;
				case 1://接到完整的数据了
					Bundle b=msg.getData();
					@SuppressWarnings("unchecked")
					ArrayList<SinglePost> list=(ArrayList<SinglePost>)b.get("list");
					RefreshUI(list);
					RemoveRefreshViews();
					break;
				case 2://遇到Exception
					RemoveRefreshViews();
					Toast.makeText(MainActivity.this, "网络错误，请检查网络设置", Toast.LENGTH_SHORT).show();  
					break;
				}
				RefreshAsyncTask.ReleaseInstance();
				refreshTask=null;
			}
		};
		appendHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0://网络太慢，10秒内没有接到数据
					if(appendTask!=null){
						appendTask.cancel(true);
						RemoveAppendViews();
						Toast.makeText(MainActivity.this, "网络连接缓慢，请稍后再试", Toast.LENGTH_SHORT).show();  
					}
					break;
				case 1://接到完整的数据了
					Bundle b=msg.getData();
					@SuppressWarnings("unchecked")
					ArrayList<SinglePost> list=(ArrayList<SinglePost>)b.get("list");
					RefreshAppendUI(list);
					RemoveAppendViews();
					break;
				case 2://遇到Exception
					RemoveAppendViews();
					Toast.makeText(MainActivity.this, "网络错误，请检查网络设置", Toast.LENGTH_SHORT).show();  
					break;
				}
				AppendAsyncTask.ReleaseInstance();
				appendTask=null;
			}
		};
		imgLoader=new AsyncImageLoader(this.getResources());
		ImageView refreshButton=(ImageView)view1.findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				StartRefreshAction();
			}
		});
		//append刷新
		TableRow more=(TableRow) view1.findViewById(R.id.tableRowMore);
		more.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				StartAppendAction();
			}
		});
	}
	
	private RefreshAsyncTask refreshTask=null;
	private AppendAsyncTask appendTask=null;
	
	private void StartRefreshAction(){
		refreshTask=RefreshAsyncTask.getInstance(refreshHandler);
		if(refreshTask != null && refreshTask.getStatus() != AsyncTask.Status.RUNNING){
			WeiboDivider d=new WeiboDivider(this);
			CampusRefreshRow r=new CampusRefreshRow(this);
			TableLayout t=(TableLayout)view1.findViewById(R.id.MainTable);
			GifView gif=(GifView)r.findViewById(R.id.RefreshView);
			gif.setGifImageType(GifImageType.COVER);
			gif.setGifImage(R.drawable.l2);
			t.addView(d, 0);
			t.addView(r, 0);
			ScrollView scroll=(ScrollView) view1.findViewById(R.id.scrollView1);
			scroll.scrollTo(0, 0);
			
			Spinner spinner=(Spinner)view1.findViewById(R.id.spinner1);
			int pos=spinner.getSelectedItemPosition();
			int id=this.getResources().getIdentifier("poi"+pos, "string", "com.example.weibonju");
			String poi=this.getResources().getString(id);
			String token=((WeiboContext)this.getApplication()).getAccessToken().getToken();
			refreshTask.execute(new String[]{token,poi});
		}
		//Intent intent=new Intent(MainActivity.this,RefreshWeiboService.class);
		//startService(intent);
	}
	
	private void StartAppendAction() {
		if(refreshTask != null && refreshTask.getStatus() == AsyncTask.Status.RUNNING){
			Toast.makeText(this, "正在刷新全局，请稍后再请求更多微博", Toast.LENGTH_LONG).show();
		}else{
			appendTask=AppendAsyncTask.getInstance(appendHandler);
			if(appendTask!=null && appendTask.getStatus() != AsyncTask.Status.RUNNING){
				TableLayout table=(TableLayout) view1.findViewById(R.id.MainTable);
				String max;
				if(table.getChildCount()==1)
					max="0";
				else{
					int index=table.getChildCount()-3;
					View v=table.getChildAt(index);
					max=(String) v.getTag();
				}
				
				TableRow row=(TableRow) view1.findViewById(R.id.tableRowMore);
				GifView gif=new GifView(this);
				gif.setGifImageType(GifImageType.COVER);
				gif.setGifImage(R.drawable.l2);
				row.addView(gif);
				
				Spinner spinner=(Spinner)view1.findViewById(R.id.spinner1);
				int pos=spinner.getSelectedItemPosition();
				int id=this.getResources().getIdentifier("poi"+pos, "string", "com.example.weibonju");
				String poi=this.getResources().getString(id);
				String token=((WeiboContext)this.getApplication()).getAccessToken().getToken();
				
				appendTask.execute(new String[]{token,poi,max});
			}
		}
	}
	
	private void RemoveRefreshViews() {
		//stopService(new Intent(MainActivity.this, RefreshWeiboService.class));
		TableLayout t=(TableLayout)view1.findViewById(R.id.MainTable);
		t.removeViewAt(0);
		t.removeViewAt(0);
	}
	
	private void RemoveAppendViews() {
		TableRow row=(TableRow) view1.findViewById(R.id.tableRowMore);
		row.removeViewAt(1);
	}

	private void RefreshUI(ArrayList<SinglePost> list) {
		TableLayout t=(TableLayout)view1.findViewById(R.id.MainTable);
		int length=t.getChildCount();
		for(int i=2;i<length-1;i++){
			t.removeViewAt(2);
		}
		nowList.clear();
		for(int i=0;i<list.size();i++){
			SinglePost p=list.get(i);
			nowList.add(p);
			insertPost(p,2+i*2,true);
		}
	}
	
	private void RefreshAppendUI(ArrayList<SinglePost> list) {
		TableLayout t=(TableLayout)view1.findViewById(R.id.MainTable);
		int start=t.getChildCount()-1;
		for(int i=0;i<list.size();i++){
			SinglePost p=list.get(i);
			nowList.add(p);
			insertPost(p,start+i*2,true);
		}
	}

	private void insertPost(SinglePost p, int i ,boolean ifNet) {
		WeiboText w=new WeiboText(this);
		w.setTag(String.valueOf(p.getPid()));
		//名字
		TextView name=(TextView) w.findViewById(R.id.nameText);
		name.setText(p.getScreen_name());
		
		String gender=p.getGender();
		if(gender.equals("f")){
			ColorStateList csl =this.getResources().getColorStateList(R.color.darkorange);
			name.setTextColor(csl);
		}
		//评论和转发
		TextView repost=(TextView) w.findViewById(R.id.numOfComAndTrans);
		repost.setText("评论 "+p.getComments_count()+" 转发 "+p.getReposts_count());
		//本体
		TextView main=(TextView) w.findViewById(R.id.mainWeibo);
		main.setText(p.getText());
		
		//日期
		TextView date=(TextView) w.findViewById(R.id.dateText);
		Date d=p.getCreated_at();
		Date now=new Date();
		int min=(int) ((now.getTime()-d.getTime())/60000);
		if(min<60)
			date.setText(min+"分钟前");
		else
		{
			Calendar cal=Calendar.getInstance();
			cal.setTime(d);
			Calendar calnow=Calendar.getInstance();
			if((cal.get(Calendar.YEAR)==calnow.get(Calendar.YEAR)) && (cal.get(Calendar.MONTH)==calnow.get(Calendar.MONTH)) && (cal.get(Calendar.DATE)==calnow.get(Calendar.DATE))){
				date.setText("今天"+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
			}else{
				date.setText(cal.get(Calendar.MONTH)+"月"+cal.get(Calendar.DAY_OF_MONTH)+"日 "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));
			}
		}
		
		//来自
		TextView from=(TextView) w.findViewById(R.id.fromText);
		from.setText("来自："+p.getSource());
		
		//赞
		TextView atti=(TextView) w.findViewById(R.id.attitudesText);
		if(p.getAttitudes_count()==0)
			atti.setText("赞+1");
		else{
			atti.setText("赞"+p.getAttitudes_count());
			ColorStateList csl =this.getResources().getColorStateList(R.color.lightcoral);
			atti.setTextColor(csl);
		}
		
		//头像
		ImageView head=(ImageView) w.findViewById(R.id.headImage);
		loadImage(ifNet,p.getProfile_image_url().toString(),String.valueOf(p.getUid()),head);
		
		//配图
		if(p.getPic_ids().length()>0){
			String[] pics=p.getPic_ids().split(",");
			if(pics.length==1){
				//一张图
				ImageView imgView=(ImageView) w.findViewById(R.id.weiboImg);
				imgView.setVisibility(View.VISIBLE);
				String url="http://ww1.sinaimg.cn/bmiddle/"+pics[0];
				loadImage(ifNet,url,pics[0],imgView);
			}else{
				//N张图
				LinearLayout ImgContainer=(LinearLayout) w.findViewById(R.id.ImgGridView);
				ImgContainer.setVisibility(View.VISIBLE);
				int setid=0;
				for(int index=1;index<=pics.length;index++){
					if(index%3==1){
						setid++;
						int setRid=this.getResources().getIdentifier("imgset"+setid, "id", "com.example.weibonju");
						LinearLayout set=(LinearLayout) w.findViewById(setRid);
						set.setVisibility(View.VISIBLE);
					}
					int imgRid=this.getResources().getIdentifier("img"+index, "id", "com.example.weibonju");
					ImageView imgView=(ImageView) w.findViewById(imgRid);
					imgView.setVisibility(View.VISIBLE);
					String url="http://ww1.sinaimg.cn/bmiddle/"+pics[index-1];
					loadImage(ifNet,url,pics[index-1],imgView);
				}
			}
		}
		
		TableLayout t=(TableLayout)view1.findViewById(R.id.MainTable);
		WeiboDivider di=new WeiboDivider(this);
		t.addView(di,i);
		t.addView(w,i);
	}
	
	private void loadImage(boolean ifNet,String url,String fileName, final ImageView view){
		Drawable drawable = imgLoader.loadDrawable(ifNet,url,fileName, new AsyncImageLoader.ImageCallback() {
            public void imageLoaded(Drawable imageDrawable) {
              view.setImageDrawable(imageDrawable);
            }
        });
       if(drawable!=null){
    	   view.setImageDrawable(drawable);
       }
	}

	@Override
	protected void onPause() {
		finishAction();
		saveConfigure();
		super.onPause();
	}
	
	private void saveConfigure() {
		Spinner spinner=(Spinner)view1.findViewById(R.id.spinner1);
		ConfigureKeeper.saveSpinnerPos(this, spinner.getSelectedItemPosition());
		PostsDB db=new PostsDB(this);
		db.saveAll(nowList);
	}

	private void finishAction(){
		if(refreshTask!=null && refreshTask.getStatus()==AsyncTask.Status.RUNNING){
    		refreshTask.cancel(true);
    		refreshTask=null;
    		RefreshAsyncTask.ReleaseInstance();
    		RemoveRefreshViews();
    	}
    	if(appendTask!=null && appendTask.getStatus()==AsyncTask.Status.RUNNING){
    		appendTask.cancel(true);
    		appendTask=null;
    		AppendAsyncTask.ReleaseInstance();
    		RemoveAppendViews();
    	}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		imgLoader.shutDown();
		Log.d(TAG, "Destroy");
	}

}
