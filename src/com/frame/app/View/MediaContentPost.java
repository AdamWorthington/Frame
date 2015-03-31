package com.frame.app.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.frame.app.R;
import com.frame.app.Core.PostPictureTask;
import com.frame.app.Model.CameraPreview;

@SuppressWarnings("deprecation")
public class MediaContentPost extends ActionBarActivity
{
	private Camera camera;
	private MediaRecorder mediaRecorder;
	private CameraPreview preview;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private boolean frontFacing = false;
	private boolean isRecording;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_preview);
		
		camera = getCameraInstance(0);
		preview = new CameraPreview(this, camera);
		FrameLayout fPreview = (FrameLayout) findViewById(R.id.camera_preview);
		fPreview.addView(preview, fPreview.getChildCount() - 1);
	}
	
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
	
	private boolean prepareVideoRecorder()
	{
		camera = getCameraInstance(0);
		mediaRecorder = new MediaRecorder();
		
		camera.unlock();
		mediaRecorder.setCamera(camera);
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
		mediaRecorder.setPreviewDisplay(preview.getHolder().getSurface());
		
		try
		{
			mediaRecorder.prepare();
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			releaseMediaRecorder();
			return false;
		}
		
		return true;
	}
	
	/*
	 * Returns the camera instance where id indicates front-facing or back facing. 0 is back, 1 is front.
	 */
	private static Camera getCameraInstance(int id)
	{
		Camera c = null;
		try
		{
			c = Camera.open(id);
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
	
	/*
	 * This function changes between edit and preview modes after taking a picture.
	 */
	private void changeModes(boolean edit)
	{
		if(edit)
		{
			//Hide the preview buttons
			Button button_changeCam = (Button) findViewById(R.id.button_changeCam);
			button_changeCam.setVisibility(View.GONE);
			Button button_capture = (Button) findViewById(R.id.button_capture);
			button_capture.setVisibility(View.GONE);
			
			//Show the edit buttons
			Button button_send = (Button) findViewById(R.id.button_send);
			button_send.setVisibility(View.VISIBLE);
			Button button_cancel = (Button) findViewById(R.id.button_cancel);
			button_cancel.setVisibility(View.VISIBLE);
		}
		else
		{
			//Hide the edit buttons
			Button button_send = (Button) findViewById(R.id.button_send);
			button_send.setVisibility(View.GONE);
			Button button_cancel = (Button) findViewById(R.id.button_cancel);
			button_cancel.setVisibility(View.GONE);
			
			//Show the preview buttons
			Button button_changeCam = (Button) findViewById(R.id.button_changeCam);
			button_changeCam.setVisibility(View.VISIBLE);
			Button button_capture = (Button) findViewById(R.id.button_capture);
			button_capture.setVisibility(View.VISIBLE);
		}
	}
	
	/*
	 * Called when a user clickers the change camera mode button. 
	 * When the camera is on front-facing mode, it changes to back.
	 * And when the camera is on back-facing mode, it changes to front.
	 */
	public void changeCamera(View view)
	{
		FrameLayout fPreview = (FrameLayout) findViewById(R.id.camera_preview);
		fPreview.removeView(preview);
		releaseCamera();
		frontFacing = !frontFacing;
		int camId = (frontFacing) ? 1 : 0;
		camera = getCameraInstance(camId);
		preview = new CameraPreview(this, camera);
		fPreview.addView(preview);
	}
	
	public void takePicture(View view)
	{
		camera.takePicture(null, null, mPicture);
		
		changeModes(true);
	}
	
	/*
	 * Called when the user clicks the capture button
	 */
	public void capture(View view)
	{
		takePicture(view);
	}
	
	public void takeVideo(View view)
	{
		if(isRecording)
		{
			mediaRecorder.stop();
			releaseMediaRecorder();
			camera.lock();
			
			isRecording = false;
		}
		else
		{
			if(prepareVideoRecorder())
			{
				mediaRecorder.start();
				isRecording = true;
			}
			else
			{
				releaseMediaRecorder();
			}
		}
	}
	
	/*
	 * Called when the user wants to send the media content to the server.
	 * After doing this, we release relevant resources and return to the main media feed.
	 */
	public void sendMediaContent(View view)
	{
		//Calling this method will release camera and media recorder resources.
		onPause();
		
		Time now = new Time();
		now.setToNow();
		String date = now.toString();
		Integer id = Integer.valueOf(0); //The id tied to the phone goes here! <---- need to implement
		String[] tags = {""};
		Integer rating = Integer.valueOf(0);
		
		new PostPictureTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				mPicture, date, id, tags, rating);
		
		//Launch the intent to return to the main page
        Intent intent = new Intent(this, MainPage.class);
        this.startActivity(intent);
		
		//Removes the activity from the back-stack.
		finish();
	}
	
	/*
	 * Method that is called when the user cancels when in edit/review mode.
	 * Returns to preview mode.
	 */
	public void cancelPostPreview(View view)
	{
		FrameLayout fPreview = (FrameLayout) findViewById(R.id.camera_preview);
		fPreview.removeView(preview);
		releaseCamera();
		int camId = (frontFacing) ? 1 : 0;
		camera = getCameraInstance(camId);
		preview = new CameraPreview(this, camera);
		fPreview.addView(preview);
		
		changeModes(false);
	}
}

