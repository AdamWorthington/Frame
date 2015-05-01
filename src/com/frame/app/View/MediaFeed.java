package com.frame.app.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.frame.app.R;
import com.frame.app.Core.JSONMessage;
import com.frame.app.Core.MediaArrayAdapter;
import com.frame.app.Core.Singleton;
import com.frame.app.Model.MediaContent;
import com.frame.app.tasks.VotePostTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MediaFeed extends Fragment
{
	private ListView listview;
	private SwipeRefreshLayout swipe = null;
	private MediaArrayAdapter adapter;
	View root;
	
	boolean sort_top;
	boolean sort_latest;
	
	private LruCache<String, Bitmap> mMemoryCache;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    // TODO Add your menu entries here
	    super.onCreateOptionsMenu(menu, inflater);
	    
	    MenuItem search = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) search.getActionView();
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) 
	    {
		    @Override
		    public boolean onQueryTextSubmit(String query) 
		    {
		    	new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", query);
		        return true;
		    }

			@Override
			public boolean onQueryTextChange(String arg0) 
			{
				return false;
			}
		});
	}



	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{				
		super.onCreateView(inflater, container, savedInstanceState);
		
		setHasOptionsMenu(true);
		View root = inflater.inflate(R.layout.fragment_media_feed, container, false);
		this.root = root;

		listview = (ListView) root.findViewById(R.id.mediaFeedListView);
		adapter = new MediaArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, Singleton.getInstance().getMediaFeed(), false);
		listview.setAdapter(adapter);
		OnItemClickListener clickListener = new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
		        Intent intent;
		        intent = new Intent(getActivity(), focusedMediaContentPage.class);
		        
		        //Load the data that this page will be displaying
		        intent.putExtra("Position", position);
		        
		        getActivity().startActivity(intent);
			}
		};
		listview.setOnItemClickListener(clickListener);
		listview.setEmptyView((LinearLayout)root.findViewById(R.id.emptyNotification));
		
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) root.findViewById(R.id.swipeMediaFeed);
		swipe = swipeView;
		swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() 
			{
				new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "");
			}
		});
		
		//Setting up the cache.
		final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
			protected int sizeOf(String key, Bitmap value)
			{
		           return value.getByteCount() /1024;
			}
		};
		
		//new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "");
		
		return root;
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) 
	{
	    menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.action_new).setVisible(true);
        menu.findItem(R.id.action_top).setVisible(true);

        super.onPrepareOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
	    switch (item.getItemId()) 
	    {
		    case R.id.action_search:
		        return true;

            case R.id.action_top:
            	Singleton.getInstance().clearAllContent();
            	adapter.notifyDataSetChanged();
                new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "", 1);
                sort_top = true;
                return true;

            case R.id.action_new:
            	Singleton.getInstance().clearAllContent();
            	adapter.notifyDataSetChanged();
                new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "");
                sort_latest = true;
                return true;


		    default:
		        break;
	    }

	    return false;
	}
	
	void handleTagsSearch()
	{
		
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	
	@Override
	public void onResume()
	{
		adapter.notifyDataSetChanged();
		
		super.onResume();
	}
	
	private class GetTask extends AsyncTask<Object, Void, JSONObject> 
	{
		@Override
		protected JSONObject doInBackground(Object... params) 
		{		
			ClientResource res = new ClientResource(params[0].toString());
			res.setMethod(Method.POST);
			
			LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			
			//Location returns null if no position is currently available. In this case, cancel the request.
			if(location == null)
			{
				
				JSONObject o = new JSONObject();
				try {
					o.put("Date", "no date");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return o;
			}
			
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();

			int bottomId = 10000; //Indicates that we want the latest.

			
			String filter = (String)params[1];
			
			JSONObject obj = JSONMessage.getPosts(bottomId, filter, Double.valueOf(latitude), Double.valueOf(longitude), 0);
			StringRepresentation stringRep = new StringRepresentation(obj.toString());

			stringRep.setMediaType(MediaType.APPLICATION_JSON);
			JSONObject o = null;
			try {
				Representation r = res.post(stringRep);
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
			
			if(JSONMessage.clientGetDate(result)[0] == "no date")
			{
				//Indicates an error.
				sendAlertFailure("We couldn't find your location. Perhaps your GPS is turned off?");
				return;
			}
			
			//Return the array of string representation pictures
			String[] pics = JSONMessage.clientGetImage(result);
			String[] dates = JSONMessage.clientGetDate(result);
			Integer[] ratings = JSONMessage.clientGetRating(result);
			Integer[] ids = JSONMessage.clientGetID(result);
			int insertIndex = 0;
			for(int i = 0; i < pics.length; i++)
			{
				//We're at the end of the stream. Return.
				if(pics[i] == "null")
					return;
				
				if(dates[i] == "null")
					continue;
				
				if(Singleton.getInstance().containsMediaContentWithId(ids[i]))
				{
					MediaContent c = Singleton.getInstance().getMediaContent(ids[i]);
					c.setRating(ratings[i]);
					continue;
				}
				
				Bitmap b = JSONMessage.decodeBase64(pics[i]);
				
				SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
				d.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = null;
				try {
					date = d.parse(dates[i]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MediaContent newContent = new MediaContent(false, ids[i].intValue(), date, b, ratings[i].intValue());
				adapter.insert(newContent, insertIndex++);
			}
			
			if(sort_top)
			{
            	adapter.sort(new Comparator<MediaContent>()
            			{
							@Override
							public int compare(MediaContent lhs,
									MediaContent rhs) 
							{
								return rhs.getRating() - lhs.getRating();
							}
            		
            			});
            	
            	sort_top = false;
			}
			
			if(sort_latest)
			{
            	adapter.sort(new Comparator<MediaContent>()
            			{
							@Override
							public int compare(MediaContent lhs,
									MediaContent rhs) 
							{
								return rhs.getTimestamp().compareTo(lhs.getTimestamp());
							}
            		
            			});
            	
            	sort_latest = false;
			}
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
