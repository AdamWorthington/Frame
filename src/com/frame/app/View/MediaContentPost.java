package com.frame.app.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.location.Location;
import android.location.LocationManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.frame.app.R;
import com.frame.app.Core.Singleton;
import com.frame.app.Model.CameraPreview;
import com.frame.app.tasks.PostPictureTask;

@SuppressWarnings("deprecation")
public class MediaContentPost extends ActionBarActivity
{
	private Camera camera;
	private MediaRecorder mediaRecorder;
	private CameraPreview preview;
    String tags1;
    private Bitmap picture;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private boolean frontFacing = false;
	private boolean isRecording;
    ArrayList<String> tags = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_preview);
		
		camera = getCameraInstance(0);
		Parameters params = camera.getParameters();
		List<Camera.Size> sizes = params.getSupportedPictureSizes();
		int num = sizes.size();
		if(num > 1)
			params.setPictureSize(sizes.get(num - 2).width, sizes.get(num - 2).height);
		camera.setParameters(params);
		preview = new CameraPreview(this, camera);
		FrameLayout fPreview = (FrameLayout) findViewById(R.id.camera_preview);
		fPreview.addView(preview, fPreview.getChildCount() - 1);

        ImageButton button = (ImageButton) findViewById(R.id.button_capture);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //videoCapture(v);
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePicture(v);
                }

            });

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
	            Bitmap temp = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix m = new Matrix();
                boolean camId = (frontFacing) ? true : false;
                if(camId)
                	m.postRotate(-90);
                else
                	m.postRotate(90);
                int widrth = temp.getWidth();
                picture = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), m, true);
                temp.recycle();
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
			ImageButton button_changeCam = (ImageButton) findViewById(R.id.button_changeCam);
			button_changeCam.setVisibility(View.GONE);
			ImageButton button_capture = (ImageButton) findViewById(R.id.button_capture);
			button_capture.setVisibility(View.GONE);
			
			//Show the edit buttons
			ImageButton button_send = (ImageButton) findViewById(R.id.button_send);
			button_send.setVisibility(View.VISIBLE);
			ImageButton button_cancel = (ImageButton) findViewById(R.id.button_cancel);
			button_cancel.setVisibility(View.VISIBLE);
			
			ImageButton button_tags = (ImageButton) findViewById(R.id.button_tag);
			button_tags.setVisibility(View.VISIBLE);
		}
		else
		{
			//Hide the edit buttons
			ImageButton button_send = (ImageButton) findViewById(R.id.button_send);
			button_send.setVisibility(View.GONE);
			ImageButton button_cancel = (ImageButton) findViewById(R.id.button_cancel);
			button_cancel.setVisibility(View.GONE);
			
			//Show the preview buttons
			ImageButton button_changeCam = (ImageButton) findViewById(R.id.button_changeCam);
			button_changeCam.setVisibility(View.VISIBLE);
			ImageButton button_capture = (ImageButton) findViewById(R.id.button_capture);
			button_capture.setVisibility(View.VISIBLE);
			
			ImageButton button_tags = (ImageButton) findViewById(R.id.button_tag);
			button_tags.setVisibility(View.GONE);
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
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }
    public void videoCapture(View view){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
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
		
		LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		//Location returns null if no position is currently available. In this case, cancel the request.
		if(location == null)
			return;
		
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		
		String id = Singleton.getInstance().getDeviceId();
		String[] tagsArray = tags.toArray(new String[tags.size()]);
		
		new PostPictureTask().execute("http://1-dot-august-clover-86805.appspot.com/Post", 
				picture, latitude, longitude, Singleton.getInstance().getDeviceId(), tags1, false);
				
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
		picture.recycle();
		releaseCamera();
		int camId = (frontFacing) ? 1 : 0;
		camera = getCameraInstance(camId);
		preview = new CameraPreview(this, camera);
		fPreview.addView(preview);
		
		changeModes(false);
	}

    public void addTags(View view)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add Tags");
        alert.setMessage("Separate tags by ','. This will allow your post to be searched later for these particular tags.");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String value = input.getText().toString();
                tags1 = value;
                tags = new ArrayList<String>(Arrays.asList(value.split("#")));
                
                for(int i = 0; i < tags.size(); i++)
                {
                	String cur = tags.get(i);
                	
                	//If it's an empty or whitespace tag, remove it. If it's length > 50, remove it
                	if(cur.length() > 50 || cur.trim().isEmpty())
                		tags.remove(i);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}

