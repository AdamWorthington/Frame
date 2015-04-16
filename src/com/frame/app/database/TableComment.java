package com.frame.app.database;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.frame.app.Model.Comment;
import com.frame.app.Model.MediaContent;

public class TableComment
{	
	public static final String TABLE_COMMENT = "comment";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_OWNERID = "ownerId";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_COMMENT
			+ "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_OWNERID + " integer, "
			+ COLUMN_TEXT + " text not null "
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
	
	public static void addComment(Comment comment, SQLiteDatabase database)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID, comment.getDbId());
		values.put(COLUMN_OWNERID, comment.getOwnerId());
		values.put(COLUMN_TEXT, comment.getValue());
		
		database.insert(TABLE_COMMENT, null, values);
		database.close();
	}
	
	public static boolean removeComment(int ownerId, SQLiteDatabase database)
	{
		boolean result = false;
		String query = "Select * FROM " + TABLE_COMMENT + " WHERE " + COLUMN_OWNERID +
				" = \"" + Integer.toString(ownerId) + "\"";
		Cursor cursor = database.rawQuery(query, null);		
		
		if(cursor.moveToFirst())
		{
			database.delete(TABLE_COMMENT, COLUMN_ID + " = ?", 
					new String[] {String.valueOf(ownerId)});
			result = true;
		}
		
		database.close();
		
		return result;
	}
}
