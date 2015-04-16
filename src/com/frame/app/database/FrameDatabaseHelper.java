package com.frame.app.database;

import java.util.ArrayList;

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
		SQLiteDatabase db = this.getReadableDatabase();
		TableComment.addComment(comment, db);
	}
	
	public void addMediaContent(MediaContent mediaContent)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		TableMediaContent.addMediaContent(mediaContent, db);
	}
	
	public ArrayList<MediaContent> getAllMediaContent()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		return TableMediaContent.getAllMediaContent(db);
	}
	
	public void removeMediaContent(MediaContent mediaContent)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		for(int i = 0; i < mediaContent.getComments().size(); i++)
			TableComment.removeComment(mediaContent.getDatabaseId(), db);
		
		TableMediaContent.removeMediaContent(mediaContent.getDatabaseId(), db);
	}
	
	public void upvoteContent(MediaContent mediaContent)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		TableMediaContent.upvoteContent(mediaContent.getDatabaseId(), mediaContent.getRating() + 1, db);
	}
	
	public void downvoteContent(MediaContent mediaContent)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		TableMediaContent.downvoteContent(mediaContent.getDatabaseId(), mediaContent.getRating() - 1, db);
	}
	
	public void flagContent(MediaContent mediaContent)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		TableMediaContent.flagContent(mediaContent, db);
	}
}
