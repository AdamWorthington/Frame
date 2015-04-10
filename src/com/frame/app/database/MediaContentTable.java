/*package com.frame.app.database;

import java.util.ArrayList;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.frame.app.Model.Comment;

public class MediaContentTable
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
			+ COLUMN_DATE + " int not null"
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
}*/
