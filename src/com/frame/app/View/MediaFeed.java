package com.frame.app.View;

import java.util.ArrayList;
import java.util.Date;

import com.frame.app.R;
import com.frame.app.Model.MediaContent;

import come.frame.app.Core.MediaArrayAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MediaFeed extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.fragment_media_feed, container, false);
		populateListView(root);
		
		return root;
	}
	
	private void populateListView(View root)
	{
		final ListView listview = (ListView) root.findViewById(R.id.mediaFeedListView);
		String[] testVals = new String[] { "Android", "iPhone", "WindowsMobile",
		        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
		        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
		        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
		        "Android", "iPhone", "WindowsMobile" };
		
		final ArrayList<MediaContent> list = new ArrayList<MediaContent>();
		for (int i = 0; i < testVals.length; ++i) 
		{
			Date d = new Date();
			MediaContent c = new MediaContent(false, testVals[i], null, d);
			list.add(c);
		}

		final MediaArrayAdapter adapter = new MediaArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		
		OnItemClickListener clickListener = new AdapterView.OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Toast.makeText(getActivity(), "clicked!", 1).show();
			}
		};
		
		listview.setOnItemClickListener(clickListener);
	}
}
