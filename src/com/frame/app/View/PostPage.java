package com.frame.app.View;

import com.frame.app.R;
import com.frame.app.R.id;
import com.frame.app.R.layout;
import com.frame.app.R.menu;
import com.frame.app.R.string;

import come.frame.app.Core.TabsListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
public class PostPage extends ActionBarActivity
{

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);

		// Set up the action bar to show a tabbed list.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab homeTab = actionBar.newTab().setText(R.string.title_Home);
		MediaFeed frag1 = new MediaFeed();
		homeTab.setTabListener(new TabsListener(frag1));

		ActionBar.Tab searchTab = actionBar.newTab().setText(R.string.title_Search);
		OtherFragment frag2 = new OtherFragment();
		searchTab.setTabListener(new TabsListener(frag2));
		
		ActionBar.Tab otherTab = actionBar.newTab().setText(R.string.title_Other);
		OtherFragment frag3 = new OtherFragment();
		otherTab.setTabListener(new TabsListener(frag3));
		
		ActionBar.Tab postTab = actionBar.newTab().setText(R.string.title_Post);
		OtherFragment frag4 = new OtherFragment();
		postTab.setTabListener(new TabsListener(frag4));
		
		actionBar.addTab(homeTab);
		actionBar.addTab(searchTab);
		actionBar.addTab(otherTab);
		actionBar.addTab(postTab);
	}

}
