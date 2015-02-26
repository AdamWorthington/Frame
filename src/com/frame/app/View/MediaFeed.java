package com.frame.app.View;

import java.util.ArrayList;

import com.frame.app.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MediaFeed extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.fragment_media_feed, container, false);

		final ListView listview = (ListView) root.findViewById(R.id.mediaFeedListView);
		String[] testVals = new String[] { "Android", "iPhone", "WindowsMobile",
		        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
		        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
		        "Android", "iPhone", "WindowsMobile" };
		
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < testVals.length; ++i) 
		{
		      list.add(testVals[i]);
		}

		
		return root;
	}
}
