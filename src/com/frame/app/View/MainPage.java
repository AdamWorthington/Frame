package com.frame.app.View;

import java.util.UUID;

import com.frame.app.R;
import com.frame.app.Core.FrameFragmentPagerAdapter;
import com.frame.app.Core.Singleton;
import com.frame.app.Core.TabsListener;
import com.frame.app.Model.MediaContent;
import com.frame.app.tasks.FlagPostTask;
import com.frame.app.tasks.PostPictureTask;
import com.frame.app.tasks.VotePostTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainPage extends ActionBarActivity
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setRequestedOrientation(1);
		
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String deviceId = deviceUuid.toString();
	    
	    Singleton.initInstance(deviceId);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
		// Set up the action bar to show a tabbed list.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
		{
            @Override
            public void onPageSelected(int position) 
            {
                // When swiping between pages, select the
                // corresponding tab.
            	getActionBar().setSelectedNavigationItem(position);
            }
        });
		mPagerAdapter = new FrameFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
				
		ActionBar.Tab homeTab = actionBar.newTab().setText(R.string.title_Home);
		TabsListener<MediaFeed> tab1 = new TabsListener<MediaFeed>(this, "home", MediaFeed.class, mViewPager);
		homeTab.setTabListener(tab1);

		/*ActionBar.Tab searchTab = actionBar.newTab().setText(R.string.title_Search);
		TabsListener<SearchPage> tab2 = new TabsListener<SearchPage>(this, "search", SearchPage.class, mViewPager);
		searchTab.setTabListener(tab2);*/
		
		ActionBar.Tab otherTab = actionBar.newTab().setText(R.string.title_Other);
		TabsListener<PeekFeed> tab3 = new TabsListener<PeekFeed>(this, "other", PeekFeed.class, mViewPager);
		otherTab.setTabListener(tab3);
		
		ActionBar.Tab postTab = actionBar.newTab().setText(R.string.title_Post);
		TabsListener<PostPage> tab4 = new TabsListener<PostPage>(this, "post", PostPage.class, mViewPager);
		postTab.setTabListener(tab4);
		
		actionBar.addTab(homeTab);
		//actionBar.addTab(searchTab);
		actionBar.addTab(otherTab);
		actionBar.addTab(postTab);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	   
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) {
	    case R.id.action_search:
	        return false;
	    case R.id.action_sort:
	    	return false;
	    default:
	        break;
	    }

	    return false;
	}
	
    public void changePage(View view){
        Intent intent;
        intent = new Intent(this,Text_post.class);
        this.startActivity(intent);

    }
    
    public void takeMediaContent(View view){
        Intent intent;
        intent = new Intent(this, MediaContentPost.class);
        this.startActivity(intent);
    }
    
    public void sendFlag(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	((ImageView) view).setImageResource(R.drawable.flagfaded);
    	RelativeLayout rl = (RelativeLayout)view.getParent();

       	TextView id = (TextView)rl.findViewById(R.id.nameOfView);
       	int intId = Integer.parseInt(id.getText().toString());
       	
       	MediaContent thisContent = Singleton.getInstance().getMediaContent(intId);
       	thisContent.setHasBeenFlagged(true);
    	
    	String user = Singleton.getInstance().getDeviceId();
    	Integer Id = Integer.valueOf(intId);
    	
    	Toast.makeText(this, "Content Flagged", Toast.LENGTH_LONG).show();
    	
		new FlagPostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id);
		
		//Singleton.getInstance().addFlaggedPicture(Id);
    }
    
    public void sendUpvote(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	RelativeLayout rl = (RelativeLayout)view.getParent();
    	ImageButton downvote = (ImageButton)rl.findViewById(R.id.downvote);
       	downvote.setEnabled(false);
    	((ImageView) view).setImageResource(R.drawable.upvotedfaded);
    	downvote.setImageResource(R.drawable.downvotefaded);
       	
       	TextView contentRating = (TextView)rl.findViewById(R.id.rating);
       	String val = (String) contentRating.getText();
       	int intVal = Integer.parseInt(val);
       	intVal += 1;
       	String newVal = String.valueOf(intVal);
       	contentRating.setText(newVal);
       	if(intVal >= 0)
       		contentRating.setTextColor(Color.GREEN);
       	
       	TextView id = (TextView)rl.findViewById(R.id.nameOfView);
       	int intId = Integer.parseInt(id.getText().toString());
       	
       	MediaContent thisContent = Singleton.getInstance().getMediaContent(intId);
       	thisContent.incrementRating();
       	thisContent.setHasBeenVoted(true);
       	
		String user = Singleton.getInstance().getDeviceId();
		Integer Id = Integer.valueOf(intId);
		Integer vote = Integer.valueOf(1);
		
		Toast.makeText(this, "Content Upvoted", Toast.LENGTH_LONG).show();
    	
		new VotePostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id, vote);
    }
    
    public void sendDownvote(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	RelativeLayout rl = (RelativeLayout)view.getParent();
    	ImageButton upvote = (ImageButton)rl.findViewById(R.id.upvote);
    	upvote.setEnabled(false);
    	((ImageView) view).setImageResource(R.drawable.downvotefaded);
    	upvote.setImageResource(R.drawable.upvotedfaded);
    	
       	TextView contentRating = (TextView)rl.findViewById(R.id.rating);
       	String val = (String) contentRating.getText();
       	int intVal = Integer.parseInt(val);
       	intVal -= 1;
       	String newVal = String.valueOf(intVal);
       	contentRating.setText(newVal);
       	if(intVal < 0)
       		contentRating.setTextColor(Color.RED);
       	
       	TextView id = (TextView)rl.findViewById(R.id.nameOfView);
       	int intId = Integer.parseInt(id.getText().toString());
       	
       	MediaContent thisContent = Singleton.getInstance().getMediaContent(intId);
       	thisContent.decrementRating();
       	thisContent.setHasBeenVoted(true);
    	
		String user = Singleton.getInstance().getDeviceId();
		Integer Id = Integer.valueOf(intId);
		Integer vote = Integer.valueOf(-1);
		
		Toast.makeText(this, "Content Downvoted", Toast.LENGTH_LONG).show();
    	
		new VotePostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id, vote);
    }

}
