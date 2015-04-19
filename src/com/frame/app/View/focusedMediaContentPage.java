package com.frame.app.View;

import java.util.ArrayList;
import java.util.Date;

import com.frame.app.R;
import com.frame.app.Core.FrameFragmentPagerAdapter;
import com.frame.app.Core.MediaArrayAdapter;
import com.frame.app.Core.Singleton;
import com.frame.app.Core.TabsListener;
import com.frame.app.Model.MediaContent;
import com.frame.app.tasks.FlagPostTask;
import com.frame.app.tasks.VotePostTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class focusedMediaContentPage extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_focused_media_content);
		
		//Get the data passed in from the other activity.
		Intent intent = getIntent();
		int pos = intent.getIntExtra("Position", 0);
		
       	MediaContent thisContent = Singleton.getInstance().getMediaContentByPos(pos);
		
		final ListView listview = (ListView) findViewById(R.id.comment_listview);
		
		TextView rating = (TextView) findViewById(R.id.rating);
		rating.setText(Integer.toString(thisContent.getRating()));
       	if(thisContent.getRating() > 0)
       		rating.setTextColor(Color.GREEN);
       	else if (thisContent.getRating() < 0)
       		rating.setTextColor(Color.RED);
       	else
       		rating.setTextColor(Color.BLACK);
		
	    TextView timestampView = (TextView) findViewById(R.id.timestamp);
	    String elapsedTime = convertTimestamp(thisContent.getTimestamp());
	    timestampView.setText(elapsedTime);
		
	    ImageView imageView = (ImageView) findViewById(R.id.contentImage);
	    imageView.setImageBitmap(thisContent.getBitmap());
	    
       	TextView id = (TextView)findViewById(R.id.nameOfView);
       	id.setText(Integer.toString(thisContent.getDatabaseId()));
       	       	
	    ImageButton upvote = (ImageButton) findViewById(R.id.upvote);
	    if(thisContent.getHasBeenVoted())
	    	upvote.setEnabled(false);
	    
	    ImageButton downvote = (ImageButton) findViewById(R.id.downvote);
	    if(thisContent.getHasBeenVoted())
	    	downvote.setEnabled(false);  
	    
	    ImageButton flag = (ImageButton) findViewById(R.id.flag);
	    if(thisContent.getHasBeenFlagged())
	    	flag.setEnabled(false);  
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
				new String[]{"hey", "yo", "Good Morning", "Hey there!", "Buenos Dias", "Hola", "Chao"});
		listview.setAdapter(adapter);
	}
	
    public void sendFlag(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	RelativeLayout rl = (RelativeLayout)view.getParent();
    	ImageButton flag = (ImageButton)rl.findViewById(R.id.flag);
    	flag.setEnabled(false);
       	
    	String user = "Craig";
    	
       	TextView id = (TextView)rl.findViewById(R.id.nameOfView);
       	int intId = Integer.parseInt(id.getText().toString());
       	
       	MediaContent thisContent = Singleton.getInstance().getMediaContent(intId);
       	thisContent.setHasBeenFlagged(true);
    	
    	Integer Id = Integer.valueOf(intId);
    	
    	Toast.makeText(this, "Content Flagged", Toast.LENGTH_LONG).show();
    	
		new FlagPostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id);
    }
    
    public void sendUpvote(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	RelativeLayout rl = (RelativeLayout)view.getParent();
    	ImageButton downvote = (ImageButton)rl.findViewById(R.id.downvote);
       	downvote.setEnabled(false);
       	
       	TextView contentRating = (TextView)rl.findViewById(R.id.rating);
       	String val = (String) contentRating.getText();
       	int intVal = Integer.parseInt(val);
       	intVal += 1;
       	String newVal = String.valueOf(intVal);
       	contentRating.setText(newVal);
       	if(intVal >= 0)
       		contentRating.setTextColor(Color.GREEN);
       	
       	TextView id = (TextView)rl.findViewById(R.id.nameOfView);
       	int intId = Integer.parseInt(id.getText().toString());
       	
       	MediaContent thisContent = Singleton.getInstance().getMediaContent(intId);
       	thisContent.incrementRating();
       	thisContent.setHasBeenVoted(true);
       	
		String user = "Craig";
		Integer Id = Integer.valueOf(intId);
		Integer vote = Integer.valueOf(1);
    	
		Toast.makeText(this, "Content Upvoted", Toast.LENGTH_LONG).show();
		
		new VotePostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id, vote);
    }
    
    public void sendDownvote(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	RelativeLayout rl = (RelativeLayout)view.getParent();
    	ImageButton upvote = (ImageButton)rl.findViewById(R.id.upvote);
    	upvote.setEnabled(false);
    	
       	TextView contentRating = (TextView)rl.findViewById(R.id.rating);
       	String val = (String) contentRating.getText();
       	int intVal = Integer.parseInt(val);
       	intVal -= 1;
       	String newVal = String.valueOf(intVal);
       	contentRating.setText(newVal);
       	if(intVal < 0)
       		contentRating.setTextColor(Color.RED);
       	
       	TextView id = (TextView)rl.findViewById(R.id.nameOfView);
       	int intId = Integer.parseInt(id.getText().toString());
       	
       	MediaContent thisContent = Singleton.getInstance().getMediaContent(intId);
       	thisContent.decrementRating();
       	thisContent.setHasBeenVoted(true);
    	
		String user = "Craig";
		Integer Id = Integer.valueOf(intId);
		Integer vote = Integer.valueOf(-1);
		
		Toast.makeText(this, "Content Downvoted", Toast.LENGTH_LONG).show();
    	
		new VotePostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id, vote);
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

    public void changePage(View view){
        Intent intent;
        intent = new Intent(this,Comment_post.class);
        this.startActivity(intent);
    }
}

