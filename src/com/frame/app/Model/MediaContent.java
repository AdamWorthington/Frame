package com.frame.app.Model;

import java.util.ArrayList;
import java.util.Date;

import android.location.Location;

/* Class representation of the media in the application */
public class MediaContent 
{
	private boolean fileType;
	private String fileId;
	private int rating;
	private int flagCount;
	private ArrayList<Comment> comments;
	private Location locationStamp;
	private Date timestamp;
	
	private int databaseId;
	
	public MediaContent(boolean fileType, String fileId, 
			Location locationStamp, Date timestamp)
	{
		this.fileType = fileType;
		this.fileId = fileId;
		this.locationStamp = locationStamp;
		this.timestamp = timestamp;
		
		this.rating = 0;
		this.flagCount = 0;
		this.databaseId = 0;
		comments = new ArrayList<Comment>();
	}
	
	/* This section contains getters and/or setters for class properties */
	public boolean getFileType()
	{
		return fileType;
	}
	
	public int getDatabaseId()
	{
		return databaseId;
	}
	
	public Location getLocation()
	{
		return locationStamp;
	}
	
	public int getRating()
	{
		return rating;
	}
	
	public void incrementRating()
	{
		this.rating++;
	}
	
	public void decrementRating()
	{
		this.rating--;
	}
	
	public int getFlagCount()
	{
		return flagCount;
	}
	
	public void incrementFlagCount()
	{
		this.flagCount++;
	}
	
	public ArrayList<Comment> getComments()
	{
		return this.comments;
	}
	
	public Location getLocationStamp()
	{
		return locationStamp;
	}
	
	public Date getTimestamp()
	{
		return timestamp;
	}
	
	/* This section contains class-specific methods */
	
	/*Linearly search through the comments and find the desired comment.
	 * If it exists, remove it. Otherwise, return.
	 */
	public void removeComment(Comment thisComment)
	{
		if(comments == null)
			return;
		
		for(Comment c : comments)
		{
			if(c.equals(thisComment))
			{
				comments.remove(thisComment);
				
				return;
			}
		}
	}
	
	public void addComment(Comment thisComment)
	{
		comments.add(thisComment);
	}
}
