package com.frame.app.Core;

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
	
	
	public MediaArrayAdapter(Context context, int resource, ArrayList<MediaContent> objects) 
	{
		super(context, resource, objects);
		
		values = objects;
		
		this.context = context;
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
	    String convertedStamp = "now";//convertTimestamp(values.get(position).getTimestamp());
	    timestampView.setText(convertedStamp);
	    
	    TextView nameView = (TextView) rowView.findViewById(R.id.nameOfView);
	    nameView.setText(Integer.toString(thisContent.getFileId()));
	    
	    TextView ratingView = (TextView) rowView.findViewById(R.id.rating);
	    int rating = thisContent.getRating();
	    ratingView.setText(String.valueOf(rating));
	    
	    ImageButton upvote = (ImageButton) rowView.findViewById(R.id.upvote);
	    if(thisContent.getHasBeenVoted())
	    	upvote.setEnabled(false);
	    
	    ImageButton downvote = (ImageButton) rowView.findViewById(R.id.downvote);
	    if(thisContent.getHasBeenVoted())
	    	downvote.setEnabled(false);  
	    
	    ImageButton flag = (ImageButton) rowView.findViewById(R.id.flag);
	    if(thisContent.getHasBeenFlagged())
	    	flag.setEnabled(false);  
	    
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
		
		
		if(timeInHours > 0)
			return Integer.toString(timeInHours) + " hours ago";
		else if(timeInMinutes > 0)
			return Integer.toString(timeInMinutes) + " minutes ago";
		else
			return Integer.toString(timeInSeconds) + " seconds ago";
		
	}

}
