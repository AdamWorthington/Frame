package com.frame.app.database;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.frame.app.Model.Comment;
import com.frame.app.Model.MediaContent;

public class TableMediaContent
{
	public static final String TABLE_MEDIACONTENT = "mediacontent";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_RATING = "rating";
	public static final String COLUMN_FLAGCOUNT = "flagcount";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_COMMENT = "comment";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_MEDIACONTENT
			+ "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_RATING + " int not null, "
			+ COLUMN_FLAGCOUNT + " int not null, "
			+ COLUMN_LATITUDE + " float not null, "
			+ COLUMN_LONGITUDE + " float not null, "
			+ COLUMN_DATE + " text not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIACONTENT);
		onCreate(database);
	}
	
	public static void addMediaContent(MediaContent mediaContent, SQLiteDatabase database)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, mediaContent.getDatabaseId());
		values.put(COLUMN_RATING, mediaContent.getRating());
		values.put(COLUMN_FLAGCOUNT, mediaContent.getFlagCount());
		values.put(COLUMN_LATITUDE, mediaContent.getLocation().getLatitude());
		values.put(COLUMN_LONGITUDE, mediaContent.getLocation().getLongitude());
		values.put(COLUMN_DATE, mediaContent.getTimestamp().toString());
		
		database.insert(TABLE_MEDIACONTENT, null, values);
		database.close();
	}
	
	public static boolean removeMediaContent(int contentId, SQLiteDatabase database)
	{
		boolean result = false;
		String query = "Select * FROM " + TABLE_MEDIACONTENT + " WHERE " + COLUMN_ID +
				" = \"" + Integer.toString(contentId) + "\"";
		Cursor cursor = database.rawQuery(query, null);		
		MediaContent mediaContent = new MediaContent();
		
		if(cursor.moveToFirst())
		{
			mediaContent.setDBId(Integer.parseInt(cursor.getString(0)));
			database.delete(TABLE_MEDIACONTENT, COLUMN_ID + " = ?", 
					new String[] {String.valueOf(contentId)});
			result = true;
		}
		
		database.close();
		
		return result;
	}
	
	public static ArrayList<MediaContent> getAllMediaContent(SQLiteDatabase database)
	{
		ArrayList<MediaContent> content = new ArrayList<MediaContent>();
		
		String selectQuery = "SELECT  * FROM " + TABLE_MEDIACONTENT;
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) 
		{
			do 
			{
				MediaContent c = new MediaContent();
				int id = cursor.getInt(0);
				int rating = cursor.getInt(1);
				int flags = cursor.getInt(2);
				float latitude = cursor.getFloat(3);
				float longitude = cursor.getFloat(4);
				String date = cursor.getString(5);
				//c.
				//content.add(c);
			} while (cursor.moveToNext());
		}
		
		return content;
	}
	
	public static void upvoteContent(int contentId, int newVal, SQLiteDatabase database)
	{
		String query = "Select * FROM " + TABLE_MEDIACONTENT + " WHERE " + COLUMN_ID +
				" = \"" + Integer.toString(contentId) + "\"";
		
		Cursor cursor = database.rawQuery(query, null);
		
		ContentValues newValue = new ContentValues();
		newValue.put(COLUMN_RATING, newVal);
		
		database.update(TABLE_MEDIACONTENT, newValue, "COLUMN_ID" + " = ?", new String[] {String.valueOf(contentId)});
	}
	
	public static void downvoteContent(int contentId, int newVal, SQLiteDatabase database)
	{
		String query = "Select * FROM " + TABLE_MEDIACONTENT + " WHERE " + COLUMN_ID +
				" = \"" + Integer.toString(contentId) + "\"";
		
		Cursor cursor = database.rawQuery(query, null);
		
		ContentValues newValue = new ContentValues();
		newValue.put(COLUMN_RATING, newVal);
		
		database.update(TABLE_MEDIACONTENT, newValue, "COLUMN_ID" + " = ?", new String[] {String.valueOf(contentId)});
	}
	
	public static void flagContent(MediaContent mediaContent, SQLiteDatabase database)
	{
		
	}
}
