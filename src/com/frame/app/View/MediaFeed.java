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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MediaFeed extends Fragment
{
	private ListView listview;
	private ArrayList<MediaContent> contentList = new ArrayList<MediaContent>();
	private SwipeRefreshLayout swipe = null;
	private MediaArrayAdapter adapter;
	View root;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{				
		View root = inflater.inflate(R.layout.fragment_media_feed, container, false);
		this.root = root;
		listview = (ListView) root.findViewById(R.id.mediaFeedListView);
		adapter = new MediaArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, contentList);
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
		
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) root.findViewById(R.id.swipeMediaFeed);
		swipe = swipeView;
		swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() 
			{
				new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get");
			}
		});
		
		return root;
	}
	
	private class GetTask extends AsyncTask<Object, Void, JSONObject> 
	{
		@Override
		protected JSONObject doInBackground(Object... params) 
		{		
			ClientResource res = new ClientResource(params[0].toString());
			res.setMethod(Method.GET);
			
			LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			
			//Location returns null if no position is currently available. In this case, cancel the request.
			if(location == null)
			{
				
				JSONObject o = new JSONObject();
				try {
					o.put("Date", "no date");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return o;
			}
			
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			int bottomId = -1; //Indicates that we want the latest.
			
			JSONObject obj = JSONMessage.getPosts(bottomId, "", Double.valueOf(latitude), Double.valueOf(longitude));
			StringRepresentation stringRep = new StringRepresentation(obj.toString());
			stringRep.setMediaType(MediaType.APPLICATION_JSON);
			JSONObject o = null;
			try {
				Representation r = res.get();
				o = new JSONObject(r.getText());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return o;
		}
		
		@Override
	    protected void onPostExecute(JSONObject result) 
		{
			swipe.setRefreshing(false);
			
			if(result == null)
			{
				//Something went wrong. We need to notify the user.
				sendAlertFailure("We were unable to retrieve any data from the server at this time.");
				return;
			}
			
			if(JSONMessage.getDate(result) == "no date")
			{
				//Indicates an error.
				sendAlertFailure("We couldn't find your location. Perhaps your GPS is turned off?");
				return;
			}
			
			String[] s = JSONMessage.clientGetImage(result);
			Bitmap b = JSONMessage.decodeBase64(s[0]);
			//ImageView iv = (ImageView)root.findViewById(R.id.contentImage);
			//iv.setImageBitmap(b);
	    }
	}
	
	private void sendAlertFailure(final String msg)
	{
		final Context context = this.getActivity();
		((Activity) context).runOnUiThread(new Runnable()
		{
			@Override
			public void run() 
			{
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
					// set title
					alertDialogBuilder.setTitle("Something went wrong");
		 
					// set dialog message
					alertDialogBuilder
						.setMessage(msg)
						.setCancelable(false)
						.setPositiveButton("Okay",new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog,int id) {
							}
						});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
		 
						// show it
						alertDialog.show();
			}
		
		});
	}
}
