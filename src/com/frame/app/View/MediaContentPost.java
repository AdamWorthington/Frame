package com.frame.app.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import android.content.pm.ActivityInfo;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.frame.app.R;
import com.frame.app.Core.PostTask;
import com.frame.app.Model.CameraPreview;
import com.frame.app.R.id;

@SuppressWarnings("deprecation")
public class MediaContentPost extends ActionBarActivity 
{
	private Camera camera;
	private MediaRecorder mediaRecorder;
	private CameraPreview preview;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	
	private PictureCallback mPicture = new PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera) 
		{
			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null){
	            Log.d("File Create", "Error creating media file, check storage permissions: ");
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            fos.write(data);
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d("File Create", "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d("File Create", "Error accessing file: " + e.getMessage());
	        }
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_preview);

		camera = getCameraInstance();
		preview = new CameraPreview(this, camera);
		FrameLayout fPreview = (FrameLayout) findViewById(R.id.camera_preview);
		fPreview.addView(preview);
		
		Button captureButton = (Button) findViewById(id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{				
				camera.takePicture(null, null, mPicture);
			}
		});
		
	}
	
	private static Camera getCameraInstance()
	{
		Camera c = null;
		try
		{
			c = Camera.open();
			Camera.Parameters params = c.getParameters();
			params.set("orientation", "portrait");
			c.setParameters(params);
			c.setDisplayOrientation(90);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return c;
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		releaseMediaRecorder();
		releaseCamera();
	}
	
	private void releaseMediaRecorder()
	{
		if(mediaRecorder != null)
		{
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			camera.lock();
		}
	}

	private void releaseCamera()
	{
		if(camera != null)
		{
			camera.release();
			camera = null;
		}
	}
	

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type)
	{
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
	
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.
	
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }
	
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	
	    return mediaFile;
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
		
		new PostTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", message);
		new GetTask().execute("http://1-dot-august-clover-86805.appspot.com/Get", message, o);

		Intent intent;
		intent = new Intent(this, MainPage.class);
		this.startActivity(intent);

	}
	
	/*public void changeCamera(View view)
	{
		camera = getCameraInstance();
		camera.
	}*/
	
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
			Toast.makeText(MediaContentPost.this, cs, 1).show();
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
