package com.frame.app.Model;

import java.util.Date;

public class Comment 
{
	private int userId;
	private Date timestamp;
	private String value;
	
	public Comment(int userId, Date timestamp, String value)
	{
		this.userId = userId;
		this.timestamp = timestamp;
		this.value = value;
	}
	
	/* This section contains getters and/or setters for class properties */
	public int getUserId()
	{
		return this.userId;
	}
	
	public Date getTimestamp()
	{
		return this.timestamp;
	}
	
	public String getValue()
	{
		return this.value;
	}
	
}
