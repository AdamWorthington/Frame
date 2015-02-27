package com.frame.app.View;

import java.util.ArrayList;

import com.frame.app.R;
import com.frame.app.Model.MediaContent;

import come.frame.app.Core.MediaArrayAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		
		final ArrayList<MediaContent> list = new ArrayList<MediaContent>();
		for (int i = 0; i < testVals.length; ++i) 
		{
			MediaContent c = new MediaContent(false,testVals[i], null, null);
		      list.add(c);
		}

		final MediaArrayAdapter adapter = new MediaArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		
		return root;
	}
}
