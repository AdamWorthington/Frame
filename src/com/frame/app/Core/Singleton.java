package com.frame.app.Core;

import java.util.ArrayList;
import java.util.UUID;

import android.telephony.TelephonyManager;

import com.frame.app.Model.MediaContent;

public class Singleton
{
	private static Singleton instance;
	
	private ArrayList<MediaContent> mediaFeed = new ArrayList<MediaContent>();
	
	private String deviceId;

	public static void initInstance(String uid)
	{
		if (instance == null)
		{
			// Create the instance
			instance = new Singleton();
			instance.deviceId = uid;
		}
	}
	
	public static Singleton getInstance()
	{
		 // Return the instance
		 return instance;
	 }
	
	 private Singleton()
	 {
	 }
	
	 public ArrayList<MediaContent> getMediaFeed()
	 {
		 return mediaFeed;
	 }
	 
	 public String getDeviceId()
	 {
		 return deviceId;
	 }
	 
	 //Gets media content by basis of DB id
	 public MediaContent getMediaContent(int id)
	 {
		 for(int i = 0; i < mediaFeed.size(); i++)
		 {
			 if(mediaFeed.get(i).getFileId() == id)
				 return mediaFeed.get(i);
		 }
		 
		 return null;
	 }
	 
	 //Gets media content by basis of position in list
	 public MediaContent getMediaContentByPos(int pos)
	 {
		return mediaFeed.get(pos); 
	 }
 }
