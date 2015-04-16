package com.frame.app.Model;

import java.util.Date;

public class Comment 
{
	private String value;
	private int dbId;
	private int ownerId;
	
	public Comment(String value)
	{
		this.value = value;
	}
	
	/* This section contains getters and/or setters for class properties */	
	public String getValue()
	{
		return this.value;
	}
	
	public int getDbId()
	{
		return this.dbId;
	}
	
	public int getOwnerId()
	{
		return this.ownerId;
	}
	
}
