package com.frame.app.Core;

import com.frame.app.View.MediaFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FrameFragmentPagerAdapter extends FragmentPagerAdapter 
{
	private static final int NUM_PAGES = 3;
	
	public FrameFragmentPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int index) 
	{ 
		Fragment thisFragment = null;
		
		switch(index)
		{
			case 0:
				thisFragment = new MediaFeed();
				break;
			case 1:
				thisFragment = new MediaFeed();
				break;
			case 2:
				thisFragment = new MediaFeed();
				break;
			case 3:
				thisFragment = new MediaFeed();
				break;
		}
		
		return (Fragment)thisFragment;
	}

	@Override
	public int getCount() 
	{
		//Number of action bar tabs = 4
		return NUM_PAGES;
	}

}
