package com.frame.app.Core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.frame.app.R;
import com.frame.app.Model.MediaContent;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MediaArrayAdapter extends ArrayAdapter<MediaContent>
{
	private final Context context;
	private final ArrayList<MediaContent> values;
	private boolean peekonly = false;
	
	public MediaArrayAdapter(Context context, int resource, ArrayList<MediaContent> objects, boolean peekonly) 
	{
		super(context, resource, objects);
		
		values = objects;
		
		this.context = context;
		this.peekonly = peekonly;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.layout_mediacontent, parent, false);
		
		MediaContent thisContent = values.get(position);
		
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.contentImage);
	    imageView.setImageBitmap(thisContent.getBitmap());

	    TextView timestampView = (TextView) rowView.findViewById(R.id.timestamp);
	    String elapsedTime = convertTimestamp(thisContent.getTimestamp());
	    timestampView.setText(elapsedTime);
	    
	    TextView nameView = (TextView) rowView.findViewById(R.id.nameOfView);
	    nameView.setText(Integer.toString(thisContent.getDatabaseId()));
	    
	    TextView ratingView = (TextView) rowView.findViewById(R.id.rating);
	    int rating = thisContent.getRating();
	    ratingView.setText(String.valueOf(rating));
	    
	    ImageButton upvote = (ImageButton) rowView.findViewById(R.id.upvote);
	    if(thisContent.getHasBeenVoted() || peekonly)
	    {
	    	upvote.setEnabled(false);
	    	upvote.setImageResource(R.drawable.upvotedfaded);
	    }
	    
	    ImageButton downvote = (ImageButton) rowView.findViewById(R.id.downvote);
	    if(thisContent.getHasBeenVoted() || peekonly)
	    {
	    	downvote.setEnabled(false);  
	    	downvote.setImageResource(R.drawable.upvotedfaded);
	    }
	    
	    ImageButton flag = (ImageButton) rowView.findViewById(R.id.flag);
	    if(thisContent.getHasBeenFlagged() || peekonly)
	    {
	    	flag.setEnabled(false);  
	    	flag.setImageResource(R.drawable.flagfaded);
	    }
	    
       	if(rating > 0)
       		ratingView.setTextColor(Color.GREEN);
       	else if (rating < 0)
       		ratingView.setTextColor(Color.RED);
       	else
       		ratingView.setTextColor(Color.BLACK);

	    return rowView;
	}
	
	/* Given a timestamp, will calculate the difference between NOW and the stamp.
	 * It will round to either the nearest seconds, minutes, or hours.
	 * For example: If the timestamp difference is less than a minute, the string will be 'x seconds' 
	 * 
	 */
	private String convertTimestamp(Date stamp)
	{
		//Will instantiate a date with the current time
		Date now = new Date();
		
		//Returns the difference in milliseconds
		long dateDifference = now.getTime() - stamp.getTime();
		
		int timeInSeconds = (int)(dateDifference / 1000);
		int timeInMinutes = timeInSeconds / 60;
		int timeInHours = timeInSeconds / 3600;
		int timeInDays = timeInHours / 24;

		if(timeInDays > 0)
			return Integer.toString(timeInHours) + " days ago";
		else if(timeInHours > 0)
			return Integer.toString(timeInHours) + " hours ago";
		else if(timeInMinutes > 0)
			return Integer.toString(timeInMinutes) + " minutes ago";
		else
			return Integer.toString(timeInSeconds) + " seconds ago";
		
	}

}
