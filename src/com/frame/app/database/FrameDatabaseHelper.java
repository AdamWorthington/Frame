/*package com.frame.app.database;

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
		MediaContentTable.onCreate(database);
	}
	
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		MediaContentTable.onUpgrade(database, oldVersion, newVersion);
	}
}
*/