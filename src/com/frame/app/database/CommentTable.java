package com.frame.app.database;

import java.util.ArrayList;
import java.util.Date;

import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.frame.app.Model.Comment;

public class CommentTable
{	
	public static final String TABLE_COMMENT = "comment";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_USER = "user";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_COMMENT
			+ "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_TEXT + " text not null, "
			+ COLUMN_USER + " text not null"
			+ ");";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
		onCreate(database);
	}
}
