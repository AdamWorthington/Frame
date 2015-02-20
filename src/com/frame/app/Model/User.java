package com.frame.app.Model;

import android.location.Location;

public class User 
{
	private int id;
	private Location location;
	private boolean isAdmin;
	
	public User(int id, boolean isAdmin)
	{
		this.id = id;
		this.isAdmin = isAdmin;
		
		location = new Location("");
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public Location getLocation()
	{
		return this.location;
	}
	
	public void setLocation(String newLoc)
	{
		location = new Location(newLoc);
	}
	
	public boolean IsAdmin()
	{
		return this.isAdmin;
	}
}
