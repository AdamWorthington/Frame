package come.frame.app.Core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.frame.app.R;
import com.frame.app.Model.MediaContent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		
	    TextView timestampView = (TextView) rowView.findViewById(R.id.timestamp);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.contentImage);
	    String convertedStamp = convertTimestamp(values.get(position).getTimestamp());
	    timestampView.setText(convertedStamp);
	    imageView.setImageResource(R.drawable.standin);
	    TextView nameView = (TextView) rowView.findViewById(R.id.nameOfView);
	    nameView.setText(values.get(position).getFileId());

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
			return Integer.toString(timeInHours) + " hours";
		else if(timeInMinutes > 0)
			return Integer.toString(timeInMinutes) + " minutes";
		else
			return Integer.toString(timeInSeconds) + " seconds";
		
	}

}
