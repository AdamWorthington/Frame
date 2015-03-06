package com.frame.app.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.frame.app.R;
import com.frame.app.Core.CustomRequest;

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
    
		
		
		
	    RequestQueue queue = Volley.newRequestQueue(this);
	    
		
		
		  Map<String, String> params = new HashMap<String, String>();
		  params.put("Text", message); 
		  params.put("Lat", "0");
		  params.put("Lon", "0"); 
		  params.put("User", "Craig");
		  params.put("Date", "1"); 
		  params.put("Tags", "1");
		  
		  CustomRequest jsObjRequest = new CustomRequest(Method.POST,
		  "http://1-dot-august-clover-86805.appspot.com/Post", params,
		  this.createRequestSuccessListener(),
		  this.createRequestErrorListener()); queue.add(jsObjRequest);
		  queue.add(jsObjRequest);
		 /* 
		 * 
		 * // -----------------------THIS SUBMITS THE TEXT //
		 * POSTS!-----------------------------//
		 * 
		 * String url1 = "http://1-dot-august-clover-86805.appspot.com/Post"; //
		 * Request a string response from the provided URL. StringRequest
		 * stringRequest = new StringRequest(Request.Method.POST, url1, null,
		 * null){
		 * 
		 * @Override protected Map<String, String> getParams() { Map<String,
		 * String> params = new HashMap<String, String>(); params.put("Text",
		 * message); params.put("Lat", "0"); params.put("Lon", "0");
		 * params.put("User", "Craig"); params.put("Date", null);
		 * params.put("Tags", null);
		 * 
		 * return params; } }; // Add the request to the RequestQueue.
		 * queue.add(stringRequest);
		 * 
		 * // -----------------------END SUMBITS TEXT //
		 * POSTS!--------------------------------------// //
		 * -----------------------THIS GETS THE TOP 10 TEXT //
		 * POSTS!-----------------------------// // Instantiate the
		 * RequestQueue.
		 * 
		 * String url2 = "http://1-dot-august-clover-86805.appspot.com/Get"; //
		 * Request a string response from the provided URL. StringRequest
		 * stringRequest2 = new StringRequest(Request.Method.GET, url2, new
		 * Response.Listener<String>() {
		 * 
		 * @Override public void onResponse(String response) { // Display the
		 * first 500 characters of the response // string.
		 * System.out.println(response); } }, new Response.ErrorListener() {
		 * 
		 * @Override public void onErrorResponse(VolleyError error) {
		 * 
		 * } }); // Add the request to the RequestQueue.
		 * queue.add(stringRequest2); // -----------------------END TOP 10 TEXT
		 * // POSTS!--------------------------------------//
		 */
		// GO back to main page
		Intent intent;
		intent = new Intent(this, MainPage.class);
		this.startActivity(intent);

	}

	private ErrorListener createMyReqErrorListener() {
		// TODO Auto-generated method stub
		return null;
	}

	private Listener<String> createMyReqSuccessListener() {
		// TODO Auto-generated method stub
		return null;
	}

	private ErrorListener createRequestErrorListener() {
		// TODO Auto-generated method stub
		return null;
	}

	private Listener<JSONObject> createRequestSuccessListener() {
		// TODO Auto-generated method stub
		return null;
	}

	public static JSONObject TextToJson(String text, Double lat, Double lon,
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
