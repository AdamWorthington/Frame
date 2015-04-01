package com.frame.app.View;

import java.util.ArrayList;
import java.util.Date;

import com.frame.app.R;
import com.frame.app.Core.FrameFragmentPagerAdapter;
import com.frame.app.Core.MediaArrayAdapter;
import com.frame.app.Core.TabsListener;
import com.frame.app.Model.MediaContent;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;

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
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{li1, li2, "Good Morning", "Hey there!"});
		listview.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

    public void changePage(View view){
        Intent intent;
        intent = new Intent(this,Text_post.class);
        this.startActivity(intent);

    }
    
    public void takeMediaContent(View view){
        Intent intent;
        intent = new Intent(this, MediaContentPost.class);
        this.startActivity(intent);
    }
    
    public void sendFlag(View view)
    {
    	int i = 0;
    	i-=1;
    }
    
    public void sendUpvote(View view)
    {
    	
    }
    
    public void sendDownvote(View view)
    {
    	
    }
    
    public void getReturnJSON()
    {
    }

}
