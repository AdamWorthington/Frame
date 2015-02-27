package com.frame.app.View;

import com.frame.app.R;

import come.frame.app.Core.TabsListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
		// Set up the action bar to show a tabbed list.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//mViewPager = (ViewPager) findViewById(R.id.pager);
		//mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() 
		//{
         //   @Override
         //   public void onPageSelected(int position) 
        //    {
                // When swiping between pages, select the
                // corresponding tab.
       //     	getActionBar().setSelectedNavigationItem(position);
        //    }
        //});
		//mPagerAdapter = new FrameFragmentPagerAdapter(getSupportFragmentManager());
		//mViewPager.setAdapter(mPagerAdapter);
				
		ActionBar.Tab homeTab = actionBar.newTab().setText(R.string.title_Home);
		TabsListener<MediaFeed> tab1 = new TabsListener<MediaFeed>(this, "home", MediaFeed.class);
		//tab1.SetViewPager(mViewPager);
		homeTab.setTabListener(tab1);

		ActionBar.Tab searchTab = actionBar.newTab().setText(R.string.title_Search);
		TabsListener<MediaFeed> tab2 = new TabsListener<MediaFeed>(this, "search", MediaFeed.class);
		//tab2.SetViewPager(mViewPager);
		searchTab.setTabListener(tab2);
		
		ActionBar.Tab otherTab = actionBar.newTab().setText(R.string.title_Other);
		TabsListener<MediaFeed> tab3 = new TabsListener<MediaFeed>(this, "other", MediaFeed.class);
		//tab3.SetViewPager(mViewPager);
		otherTab.setTabListener(tab3);
		
		ActionBar.Tab postTab = actionBar.newTab().setText(R.string.title_Post);
		TabsListener<MediaFeed> tab4 = new TabsListener<MediaFeed>(this, "post", MediaFeed.class);
		//tab4.SetViewPager(mViewPager);
		postTab.setTabListener(tab4);
		
		actionBar.addTab(homeTab);
		actionBar.addTab(searchTab);
		actionBar.addTab(otherTab);
		actionBar.addTab(postTab);
	}

}
