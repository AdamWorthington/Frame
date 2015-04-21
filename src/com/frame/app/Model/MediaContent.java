package com.frame.app.Model;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.location.Location;

/* Class representation of the media in the application */
public class MediaContent 
{
	private boolean fileType;
	private int rating;
	private int flagCount;
	private ArrayList<Comment> comments;
	private Date timestamp;
	private Bitmap bitmap;
	
	private boolean hasBeenVoted = false;
	private boolean hasBeenFlagged = false;
	
	private int databaseId;
	
	public MediaContent(boolean fileType, int dbId, Date timestamp, Bitmap bitmap, int rating)
	{
		this.fileType = fileType;
		this.timestamp = timestamp;
		this.bitmap = bitmap;
		
		this.rating = rating;
		this.flagCount = 0;
		this.databaseId = dbId;
		comments = new ArrayList<Comment>();
	}
	
	public MediaContent() {
		// TODO Auto-generated constructor stub
	}

	/* This section contains getters and/or setters for class properties */
	public boolean getFileType()
	{
		return fileType;
	}
	
	public Bitmap getBitmap()
	{
		return bitmap;
	}
	
	public int getDatabaseId()
	{
		return databaseId;
	}
	
	public boolean getHasBeenFlagged()
	{
		return hasBeenFlagged;
	}
	
	public void setHasBeenFlagged(boolean value)
	{
		hasBeenFlagged = value;
	}
	
	public boolean getHasBeenVoted()
	{
		return hasBeenVoted;
	}
	
	public void setHasBeenVoted(boolean value)
	{
		hasBeenVoted = value;
	}
	
	public int getRating()
	{
		return rating;
	}
	
	public void incrementRating()
	{
		this.rating++;
	}
	
	public void setRating(int value)
	{
		this.rating = value;
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
	
	public Date getTimestamp()
	{
		return timestamp;
	}
	
	public int getDbId()
	{
		return databaseId;
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
