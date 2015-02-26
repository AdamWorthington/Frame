package come.frame.app.Core;

import com.frame.app.View.MediaFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FrameFragmentPagerAdapter extends FragmentPagerAdapter 
{
	private static final int NUM_PAGES = 5;
	
	public FrameFragmentPagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) 
	{ 
		return null;
	}

	@Override
	public int getCount() 
	{
		//Number of action bar tabs = 4
		return NUM_PAGES;
	}

}
