package com.frame.app.Model;

import java.util.Date;
import android.location.Location;

public class VideoContent extends MediaContent 
{
	
	
	public VideoContent(boolean fileType, String fileId, 
			Location locationStamp, Date timestamp)
	{
		super(fileType, fileId, locationStamp, timestamp);
	}
	
	
}
