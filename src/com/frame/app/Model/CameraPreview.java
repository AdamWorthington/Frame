package com.frame.app.Model;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback
{
	private SurfaceHolder holder;
	private Camera camera;
	
	public CameraPreview(Context context, Camera cam)
	{
		super(context);
		camera = cam;
		
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder)
	{
		try 
		{
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
    public void surfaceDestroyed(SurfaceHolder holder) 
    {
        // empty. Take care of releasing the Camera preview in your activity.
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) 
    {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (holder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try 
        {
            camera.stopPreview();
        } catch (Exception e)
        {
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try 
        {
        	camera.setPreviewDisplay(holder);
        	camera.startPreview();

        } catch (Exception e){
            Log.d("cam", "Error starting camera preview: " + e.getMessage());
        }
    }
}
