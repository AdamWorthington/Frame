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
import android.graphics.Bitmap;
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

import com.frame.app.Core.ImageConverter;
import com.frame.app.Core.Singleton;
import com.frame.app.R;
import com.frame.app.tasks.PostPictureTask;
import com.frame.app.tasks.PostTask;

public class Text_post extends ActionBarActivity {

	protected static final String ERROR_TAG = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_post);
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

	public void changeToMain(View view) {
		Intent intent;
		intent = new Intent(this, MainPage.class);
		this.startActivity(intent);

	}

	String message = "";

	public void changeToMainAfterPosting(View view)
			throws ClientProtocolException, IOException {
		// Grab the text field
		EditText editText = (EditText) findViewById(R.id.editText);
		message = editText.getText().toString();
		
		JSONObject o = null;
		
		//new PostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", message);
		//new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", message, o);
        Bitmap b = ImageConverter.textToImage(message);


        String[] tags = {""};

        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //Location returns null if no position is currently available. In this case, cancel the request.
        if(location == null)
            return;

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        String id = Singleton.getInstance().getDeviceId();

        new PostPictureTask().execute("http://1-dot-august-clover-86805.appspot.com/Post",
                b, latitude, longitude, Singleton.getInstance().getDeviceId(), tags, true);



		Intent intent;
		intent = new Intent(this, MainPage.class);
		this.startActivity(intent);

	}
	
	public void setReturnJSON(JSONObject o)
	{
		EditText editText = (EditText) findViewById(R.id.editText);
		String s =  o.toString();
		CharSequence cs = s;
		Toast.makeText(this, cs, 1).show();
	}
	
	private class GetTask extends AsyncTask<Object, Void, JSONObject> 
	{
		@Override
		protected JSONObject doInBackground(Object... params) 
		{		
			ClientResource res = new ClientResource(params[0].toString());
			res.setMethod(Method.GET);
			JSONObject obj = textToJson("", 0.0, 0.0, "", "", new String[]{""});
			StringRepresentation stringRep = new StringRepresentation(
					obj.toString());
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
			String s =  result.toString();
			CharSequence cs = s;
			Toast.makeText(Text_post.this, cs, 1).show();
	    }
		
		private JSONObject textToJson(String text, Double lat, Double lon,
				String user, String date, String[] tags) {
			JSONObject jo = new JSONObject();

			try {
				jo.put("Text", text);
				jo.put("Lat", lat);
				jo.put("Lon", lon);
				jo.put("User", user);
				jo.put("Date", date);
				jo.put("Tags", tags);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return jo;
		}
	}
}
