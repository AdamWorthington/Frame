package com.frame.app.View;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.frame.app.R;
import com.frame.app.Core.Singleton;
import com.frame.app.tasks.PostCommentTask;
import com.frame.app.tasks.PostPictureTask;
import com.frame.app.tasks.PostTask;

public class Comment_post extends ActionBarActivity {

    protected static final String ERROR_TAG = null;
    private Integer picId;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);
        
        Intent intent = getIntent();
        picId = intent.getIntExtra("Id", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_text_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void CancelPage(View view) 
    {
    	finish();
    }
    
    public void changeToFocusedAfterPosting(View view)
    {
		String id = Singleton.getInstance().getDeviceId();
    	
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
				
		new PostCommentTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				message, id, picId);

		//Removes the activity from the back-stack.
		finish();

    }
}
