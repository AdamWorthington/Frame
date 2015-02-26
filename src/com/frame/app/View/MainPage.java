package com.frame.app.View;

import com.frame.app.R;
import com.frame.app.R.id;
import com.frame.app.R.layout;
import com.frame.app.R.menu;
import com.frame.app.R.string;

import come.frame.app.Core.FrameFragmentPagerAdapter;
import come.frame.app.Core.TabsListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		
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

		// Set up the action bar to show a tabbed list.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab homeTab = actionBar.newTab().setText(R.string.title_Home);
		MediaFeed frag1 = new MediaFeed();
		TabsListener tab1 = new TabsListener(frag1);
		tab1.SetViewPager(mViewPager);
		homeTab.setTabListener(tab1);

		ActionBar.Tab searchTab = actionBar.newTab().setText(R.string.title_Search);
		OtherFragment frag2 = new OtherFragment();
		TabsListener tab2 = new TabsListener(frag2);
		tab2.SetViewPager(mViewPager);
		searchTab.setTabListener(tab2);
		
		ActionBar.Tab otherTab = actionBar.newTab().setText(R.string.title_Other);
		OtherFragment frag3 = new OtherFragment();
		TabsListener tab3 = new TabsListener(frag3);
		tab3.SetViewPager(mViewPager);
		otherTab.setTabListener(tab3);
		
		ActionBar.Tab postTab = actionBar.newTab().setText(R.string.title_Post);
		OtherFragment frag4 = new OtherFragment();
		TabsListener tab4 = new TabsListener(frag4);
		tab4.SetViewPager(mViewPager);
		postTab.setTabListener(tab4);
		
		actionBar.addTab(homeTab);
		actionBar.addTab(searchTab);
		actionBar.addTab(otherTab);
		actionBar.addTab(postTab);
	}

}
