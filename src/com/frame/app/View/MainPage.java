package com.frame.app.View;

import com.frame.app.R;

import come.frame.app.Core.FrameFragmentPagerAdapter;
import come.frame.app.Core.TabsListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;

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

}
