package com.frame.app.database;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/*public class FrameContentProvider extends ContentProvider
{
	 // database
	  private FrameDatabaseHelper database;

	  // used for the UriMacher
	  private static final int TODOS = 10;
	  private static final int TODO_ID = 20;
	  private static final int COMMENTS = 30;
	  private static final int COMMENT_ID = 40;

	  private static final String AUTHORITY = "com.frame.app.database.contentprovider";

	  private static final String BASE_PATH_MEDIACONTENT = "mediacontents";
	  private static final String BASE_PATH_COMMENT = "comments";
	  
	  public static final Uri CONTENT_URI_MEDIACONTENT = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH_MEDIACONTENT);
	  public static final Uri Content_URI_COMMENTS = Uri.parse("content://" + AUTHORITY
		      + "/" + BASE_PATH_COMMENT);

	  public static final String CONTENT_TYPE_MEDIACONTENT = ContentResolver.CURSOR_DIR_BASE_TYPE
	      + "/mediacontents";
	  public static final String CONTENT_ITEM_TYPE_MEDIACONTENT = ContentResolver.CURSOR_ITEM_BASE_TYPE
	      + "/mediacontent";
	  
	  public static final String CONTENT_TYPE_COMMENT = ContentResolver.CURSOR_DIR_BASE_TYPE
		      + "/comments";
		  public static final String CONTENT_ITEM_TYPE_COMMENT = ContentResolver.CURSOR_ITEM_BASE_TYPE
		      + "/comment";

	  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH_MEDIACONTENT, TODOS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH_MEDIACONTENT + "/#", TODO_ID);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH_COMMENT, COMMENTS);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH_COMMENT + "/#", COMMENT_ID);
	  }

	  @Override
	  public boolean onCreate() 
	  {
	    database = new FrameDatabaseHelper(getContext());
	    return false;
	  }

	  @Override
	  public Cursor query(Uri uri, String[] projection, String selection,
	      String[] selectionArgs, String sortOrder) 
	  {

	    // Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    // check if the caller has requested a column which does not exists
	    checkColumns(projection);

	    // Set the table
	    queryBuilder.setTables(MediaContentTable.TABLE_MEDIACONTENT);
	    queryBuilder.setTables(CommentTable.TABLE_COMMENT);

	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) 
	    {
		    case TODOS:
		        break;
		    case TODO_ID:
		        // adding the ID to the original query
		        queryBuilder.appendWhere(MediaContentTable.COLUMN_ID + "=" + uri.getLastPathSegment());
		      	break;
		    case COMMENTS:
		    	break;
			case COMMENT_ID:
				// adding the ID to the original query
			    queryBuilder.appendWhere(CommentTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			    break;
		    default:
		    	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }

	    SQLiteDatabase db = database.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    // make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	  }

	  @Override
	  public String getType(Uri uri)
	  {
		  return null;
	  }

	  @Override
	  public Uri insert(Uri uri, ContentValues values) 
	  {
		  int uriType = sURIMatcher.match(uri);
		  SQLiteDatabase sqlDB = database.getWritableDatabase();
		  int rowsDeleted = 0;
		  long id = 0;
		  String path;
		  switch (uriType) 
		  {
			  case TODOS:
			   	  id = sqlDB.insert(MediaContentTable.TABLE_MEDIACONTENT, null, values);
			   	  path = BASE_PATH_MEDIACONTENT;
			      break;
			  case COMMENTS:
			   	  id = sqlDB.insert(CommentTable.TABLE_COMMENT, null, values);
			   	  path = BASE_PATH_COMMENT;
				  break;
			  default:
			    throw new IllegalArgumentException("Unknown URI: " + uri);
		  }
		  
		  getContext().getContentResolver().notifyChange(uri, null);
		  return Uri.parse(path + "/" + id);
	  }

	  @Override
	  public int delete(Uri uri, String selection, String[] selectionArgs) 
	  {
		  int uriType = sURIMatcher.match(uri);
		  SQLiteDatabase sqlDB = database.getWritableDatabase();
	      int rowsDeleted = 0;
	      switch (uriType) 
	      {
	      	case TODOS:
	      		rowsDeleted = sqlDB.delete(MediaContentTable.TABLE_MEDIACONTENT, selection,
	      				selectionArgs);
	      		break;
	      	case TODO_ID:
	      		String id = uri.getLastPathSegment();
	      		if (TextUtils.isEmpty(selection)) 
	      		{
	      			rowsDeleted = sqlDB.delete(MediaContentTable.TABLE_MEDIACONTENT,
	      					MediaContentTable.COLUMN_ID + "=" + id, null);
	      		} 
	      		else 
	      		{
	      			rowsDeleted = sqlDB.delete(MediaContentTable.TABLE_MEDIACONTENT,
	      					MediaContentTable.COLUMN_ID + "=" + id 
	      					+ " and " + selection, selectionArgs);
	      		}
	      		break;
	      	case COMMENTS:
	      		rowsDeleted = sqlDB.delete(CommentTable.TABLE_COMMENT, selection,
	      				selectionArgs);
	      		break;
	      	case COMMENT_ID:
	      		String id2 = uri.getLastPathSegment();
	      		if (TextUtils.isEmpty(selection)) 
	      		{
	      			rowsDeleted = sqlDB.delete(CommentTable.TABLE_COMMENT,
	      					CommentTable.COLUMN_ID + "=" + id2, null);
	      		} 
	      		else 
	      		{
	      			rowsDeleted = sqlDB.delete(CommentTable.TABLE_COMMENT,
	      					CommentTable.COLUMN_ID + "=" + id2 
	      					+ " and " + selection, selectionArgs);
	      		}
	      		break;
	      	default:
	      		throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	      
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	  }

	  @Override
	  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
	  {
		  int uriType = sURIMatcher.match(uri);
		  SQLiteDatabase sqlDB = database.getWritableDatabase();
		  int rowsUpdated = 0;
		  switch (uriType) 
		  {
		  		case TODOS:
				    rowsUpdated = sqlDB.update(MediaContentTable.TABLE_MEDIACONTENT, 
						  values, selection, selectionArgs);
				    break;
		        case TODO_ID:
		    	    String id = uri.getLastPathSegment();
		    	    if (TextUtils.isEmpty(selection)) 
		    	    {
		    		    rowsUpdated = sqlDB.update(MediaContentTable.TABLE_MEDIACONTENT, values, MediaContentTable.COLUMN_ID + "=" + id, null);
		    	    } 
		    	    else 
		    	    {
		    		    rowsUpdated = sqlDB.update(MediaContentTable.TABLE_MEDIACONTENT, 
			            	values,
			            	MediaContentTable.COLUMN_ID + "=" + id 
			            	+ " and " 
			            	+ selection,
			            	selectionArgs);
			        }
		    	    break;
		  		case COMMENTS:
				    rowsUpdated = sqlDB.update(CommentTable.TABLE_COMMENT, 
						  values, selection, selectionArgs);
				    break;
		        case COMMENT_ID:
		    	    String id2 = uri.getLastPathSegment();
		    	    if (TextUtils.isEmpty(selection)) 
		    	    {
		    		    rowsUpdated = sqlDB.update(CommentTable.TABLE_COMMENT, values, CommentTable.COLUMN_ID + "=" + id2, null);
		    	    } 
		    	    else 
		    	    {
		    		    rowsUpdated = sqlDB.update(CommentTable.TABLE_COMMENT, values,
		    		    		CommentTable.COLUMN_ID + "=" + id2 
			            	+ " and " 
			            	+ selection,
			            	selectionArgs);
			        }
		    	    break;
		        default:
		        	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
		  
	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	  }
	  
	  private void checkColumns(String[] projection) 
	  {
		  String[] available = { MediaContentTable.COLUMN_RATING,
	    		MediaContentTable.COLUMN_FLAGCOUNT, MediaContentTable.COLUMN_LATITUDE,
	    		MediaContentTable.COLUMN_LONGITUDE, MediaContentTable.COLUMN_ID, MediaContentTable.COLUMN_DATE, MediaContentTable.COLUMN_COMMENT };
	    
	    if (projection != null) 
	    {
	    	HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
	    	HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
	        // check if all columns which are requested are available
	        if (!availableColumns.containsAll(requestedColumns)) 
	        {
	        	throw new IllegalArgumentException("Unknown columns in projection");
	        }
	     }
	  }
}*/
