package com.frame.app.View;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
import com.frame.app.Core.Singleton;
import com.frame.app.Model.MediaContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PeekFeed extends Fragment
{
	private ListView listview;
	private SwipeRefreshLayout swipe = null;
	private MediaArrayAdapter adapter;
	private MenuItem searchItem;
	private int sortVal = 0;
	private Address address;
	View root;
	
	boolean sort_top;
	boolean sort_latest;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		View root = inflater.inflate(R.layout.fragment_other, container, false);
		this.root = root;

		listview = (ListView) root.findViewById(R.id.peekMediaFeedListView);
		adapter = new MediaArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, Singleton.getInstance().getPeekMediaFeed(), true);
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
		        intent.putExtra("Interactable", false);
		        
		        getActivity().startActivity(intent);
			}
		};
		listview.setOnItemClickListener(clickListener);
		listview.setEmptyView((LinearLayout)root.findViewById(R.id.emptyNotification));
		
		final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) root.findViewById(R.id.swipePeekMediaFeed);
		swipe = swipeView;
		swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() 
			{
				Singleton.getInstance().clearAllPeekContent();
            	adapter.notifyDataSetChanged();
				new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", address, sortVal);
			}
		});
		
		promptForLocation();
		
		//new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "", sortVal);
		
		return root;
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) 
	{
	    menu.findItem(R.id.action_search).setVisible(false);
	    menu.findItem(R.id.action_top).setVisible(true);
	    menu.findItem(R.id.action_new).setVisible(true);
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
            	Singleton.getInstance().clearAllPeekContent();
            	adapter.notifyDataSetChanged();
                new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "", 1);
                sort_top = true;
                return true;

            case R.id.action_new:
            	Singleton.getInstance().clearAllPeekContent();
            	adapter.notifyDataSetChanged();
                new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", "");
                sort_latest = true;
                return true;


		    default:
		        break;
	    }

	    return false;
	}
	
	private class GetTask extends AsyncTask<Object, Void, JSONObject> 
	{
		@Override
		protected JSONObject doInBackground(Object... params) 
		{		
			Address requestAddress = (Address) params[1];
			
			ClientResource res = new ClientResource(params[0].toString());
			res.setMethod(Method.POST);
			
			double latitude = 0;
			double longitude = 0;
			
			//Location returns null if no position is currently available. In this case, cancel the request.
			if(requestAddress == null)
			{
				
				JSONObject o = new JSONObject();
				try {
					o.put("ID", -1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return o;
			}
			else
			{
				latitude = requestAddress.getLatitude();
				longitude = requestAddress.getLongitude();
			}
			

			int bottomId = 10000; //Indicates that we want the latest.
			
			int sort = ((Integer)params[2]).intValue();
			
			JSONObject obj = JSONMessage.getPosts(bottomId, "", Double.valueOf(latitude), Double.valueOf(longitude), sort);
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
						
			if(JSONMessage.getID(result) == -1)
			{
				//Indicates an error.
				sendAlertFailure("We couldn't find that location. Did you spell the address correctly?");
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
					adapter.notifyDataSetChanged();
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
				adapter.notifyDataSetChanged();
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
	
	private void promptForLocation()
	{
		final Context c = this.getActivity();
        AlertDialog.Builder alert = new AlertDialog.Builder(c);

        alert.setTitle("Peek!");
        alert.setMessage("Enter a location and we'll grab the latest frames for you.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this.getActivity());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String value = input.getText().toString();
                
    			Geocoder geocoder = new Geocoder(c, Locale.US);
    		    List<Address> listOfAddress;
    		    
    		    try {
    		        listOfAddress = geocoder.getFromLocationName(value, 1);
    		        if(listOfAddress != null && !listOfAddress.isEmpty())
    		        {
	    		        address = listOfAddress.get(0);
	
	    		        String country = address.getCountryCode();
	    		        String adminArea= address.getAdminArea();
	    		        String locality= address.getLocality();

    		        }
    		    } catch (IOException e) {
    		        e.printStackTrace();
    		    }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
	}
}

