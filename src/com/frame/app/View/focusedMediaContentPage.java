package com.frame.app.View;

import java.util.ArrayList;
import java.util.Date;

import com.frame.app.R;
import com.frame.app.Core.FlagPostTask;
import com.frame.app.Core.FrameFragmentPagerAdapter;
import com.frame.app.Core.MediaArrayAdapter;
import com.frame.app.Core.TabsListener;
import com.frame.app.Core.VotePostTask;
import com.frame.app.Model.MediaContent;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		String li1 = intent.getStringExtra("ListItem1");
		String li2 = intent.getStringExtra("ListItem2");
		
		final ListView listview = (ListView) findViewById(R.id.comment_listview);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
				new String[]{li1, li2, "Good Morning", "Hey there!", "Buenos Dias", "Hola", "Chao"});
		listview.setAdapter(adapter);
	}
    
    public void sendFlag(View view)
    {
    	//Disable the button
    	view.setEnabled(false);
    	
    	String user = "Craig";
    	Integer id = Integer.valueOf(0);
    	
		new FlagPostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, id);
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
       	
		String user = "Craig";
		Integer Id = Integer.valueOf(0);
		Integer vote = Integer.valueOf(1);
    	
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
    	
		String user = "Craig";
		Integer Id = Integer.valueOf(0);
		Integer vote = Integer.valueOf(-1);
    	
		new VotePostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				user, Id, vote);
    }
}
