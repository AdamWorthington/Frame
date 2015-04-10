package com.frame.app.View;

import com.frame.app.R;
import com.frame.app.Core.FrameFragmentPagerAdapter;
import com.frame.app.Core.TabsListener;
import com.frame.app.tasks.FlagPostTask;
import com.frame.app.tasks.PostPictureTask;
import com.frame.app.tasks.VotePostTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    	
    	String user = "Craig";
    	Integer id = Integer.valueOf(0);
    	
		new FlagPostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, id);
    }
    
    public void sendUpvote(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	RelativeLayout rl = (RelativeLayout)view.getParent();
    	ImageButton downvote = (ImageButton)rl.findViewById(R.id.downvote);
       	downvote.setEnabled(false);
       	
       	TextView contentRating = (TextView)rl.findViewById(R.id.rating);
       	String val = (String) contentRating.getText();
       	int intVal = Integer.parseInt(val);
       	intVal += 1;
       	String newVal = String.valueOf(intVal);
       	contentRating.setText(newVal);
       	if(intVal >= 0)
       		contentRating.setTextColor(Color.GREEN);
       	
		String user = "Craig";
		Integer Id = Integer.valueOf(5);
		Integer vote = Integer.valueOf(1);
    	
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
    	
       	TextView contentRating = (TextView)rl.findViewById(R.id.rating);
       	String val = (String) contentRating.getText();
       	int intVal = Integer.parseInt(val);
       	intVal -= 1;
       	String newVal = String.valueOf(intVal);
       	contentRating.setText(newVal);
       	if(intVal < 0)
       		contentRating.setTextColor(Color.RED);
    	
		String user = "Craig";
		Integer Id = Integer.valueOf(0);
		Integer vote = Integer.valueOf(-1);
    	
		new VotePostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id, vote);
    }
    
    public void getReturnJSON()
    {
    }

}
