package com.frame.app.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.frame.app.R;
import com.frame.app.Core.JSONMessage;
import com.frame.app.Core.MediaArrayAdapter;
import com.frame.app.Model.MediaContent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) root.findViewById(R.id.swipeMediaFeed);
		swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() 
			{
				//Put refresh code here!
				new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get");

				swipeView.setRefreshing(false);
			}
		});
		
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
		        Intent intent;
		        intent = new Intent(getActivity(), focusedMediaContentPage.class);
		        
		        //Load the data that this page will be displaying
		        intent.putExtra("ListItem1", "Hello");
		        intent.putExtra("ListItem2", "Goodbye");
		        
		        getActivity().startActivity(intent);
			}
		};
		
		listview.setOnItemClickListener(clickListener);
	}
	
	private class GetTask extends AsyncTask<Object, Void, JSONObject> 
	{
		@Override
		protected JSONObject doInBackground(Object... params) 
		{		
			ClientResource res = new ClientResource(params[0].toString());
			res.setMethod(Method.GET);
			
			/*LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			//Location returns null if no position is currently available. In this case, cancel the request.
			if(location == null)
			{
				SwipeRefreshLayout swipeView = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeMediaFeed);
				swipeView.setRefreshing(false);
				return null;
			}*/
			
			double longitude = 0;//location.getLongitude();
			double latitude = 0;//location.getLatitude();
			Double lon = Double.valueOf(longitude);
			Double lat = Double.valueOf(latitude);
			int bottomId = -1; //Indicates that we want the latest.
			
			JSONObject obj = JSONMessage.getPosts(bottomId, "", lat, lon);
			StringRepresentation stringRep = new StringRepresentation(
					obj.toString());
			stringRep.setMediaType(MediaType.APPLICATION_JSON);
			try {
				Representation r = res.post(stringRep);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
	    protected void onPostExecute(JSONObject result) 
		{
			if(result == null)
			{
				//Something went wrong
				return;
			}
			
			String[] s = JSONMessage.clientGetImage(result);
			Bitmap b = JSONMessage.decodeBase64(s[0]);
			
		    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "MyCameraApp");
		    // This location works best if you want the created images to be shared
		    // between applications and persist after your app has been uninstalled.
		
		    // Create the storage directory if it does not exist
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("MyCameraApp", "failed to create directory");
		            return;
		        }
		    }
			
			 FileOutputStream out = null;
	         File imageFile = new File(mediaStorageDir, String.format("%s.jpg",
	                    "from server2"));
	            try {
			    out = new FileOutputStream(imageFile);
			    b.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
			    // PNG is a lossless format, the compression factor (100) is ignored
			} catch (Exception e) {
			    e.printStackTrace();
			} finally {
			    try {
			        if (out != null) {
			            out.close();
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
	    }
	}
}
