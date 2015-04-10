package com.frame.app.database;

import com.frame.app.Model.Comment;
import com.frame.app.Model.MediaContent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FrameDatabaseHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "frametable.db";
	private static final int DATABASE_VERSION = 1;
	
	//Table names
	private static final String TABLE_MEDIACONTENT = "mediacontents";
	private static final String TABLE_COMMENT = "comments";
	
	public FrameDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		TableMediaContent.onCreate(database);
		TableComment.onCreate(database);
	}
	
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		TableMediaContent.onUpgrade(database, oldVersion, newVersion);
		TableComment.onUpgrade(database, oldVersion, newVersion);
	}
	
	public void addComment(Comment comment)
	{
		TableComment.addComment(comment);
	}
	
	public void addMediaContent(MediaContent mediaContent)
	{
		TableMediaContent.addMediaContent(mediaContent, null);
	}
	
	public void removeMediaContent(MediaContent mediaContent)
	{
		//TableMediaContent.removeMediaContent(mediaContent, null);
	}
	
	public void upvoteContent(MediaContent mediaContent)
	{
		//TableMediaContent.upvoteContent(mediaContent, null);
	}
	
	public void downvoteContent(MediaContent mediaContent)
	{
		//TableMediaContent.downvoteContent(mediaContent, null);
	}
	
	public void flagContent(MediaContent mediaContent)
	{
		//TableMediaContent.flagContent(mediaContent, null);
	}
}
