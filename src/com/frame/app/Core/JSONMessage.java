package com.frame.app.Core;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;



public class JSONMessage 
{
	public static Bitmap decodeBase64(String input)  
	{ 
		byte[] decodedByte = Base64.decode(input, 0); 
		
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, o);
		o.inSampleSize = calculateInSampleSize(o, 300, 300);
		o.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, o);  
	} 
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) 
	{
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) 
	    {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) 
	        {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	
	//FRONTEND
	public static JSONObject commentToJson(Integer id, String comment, String user) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("POST", 1);
			jo.put("ID", id);
			jo.put("Comment", comment);
			jo.put("User", user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
	
	//FRONTEND
	public static JSONObject getCommentsFromDatabase(Integer postID) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("GET", 1);
			jo.put("Comment", 1);
			jo.put("ID", postID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}

	public static String encodeTobase64(Bitmap image, Boolean text)
 	{ 
 	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(!text){
            image.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        }
        else{
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        }
        byte[] b = baos.toByteArray();
	    String toReturn = Base64.encodeToString(b,Base64.DEFAULT); 
 	    return toReturn; 
 	} 

	
	//create json media messages to be sent to client 
 	public static JSONObject clientPictureToJson(Bitmap pic, Double lat, Double lon, String user, String[] tags, Boolean text)
 	{ 
 		JSONObject jo = new JSONObject(); 
 		try 
 		{ 
 			String picS = encodeTobase64(pic, text);
 			jo.put("POST", 1);
 			jo.put("Picture", picS); 
 			jo.put("Lat",lat); 
 			jo.put("Lon",lon); 
 			jo.put("User", user); 
 			jo.put("Tags", tags); 
		} 
 		catch (JSONException e) { 
 			e.printStackTrace(); 
 		} 
 		 
 		return jo; 
 	} 

	
	public static JSONObject isSuccess(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("Success", 1);
		} catch (JSONException e) {
			System.err.println("Failed making success packet");
		}
		return jo;
	}
	
	public static JSONObject clientTextToJson(String text, Double lat, Double lon, String user,String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			//Bitmap bmp = ImageConverter.textToImage(text);
			String picS = ""; //encodeTobase64(bmp);
			jo.put("POST", 1);
			jo.put("Picture", picS);
			jo.put("Lat",lat);
			jo.put("Lon",lon);
			jo.put("User", user);
			jo.put("Tags", tags);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	
		return jo;
	}
	
	public static JSONObject clientVideoToJson(Object vid, Double lat, Double lon, String user, String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("POST", 1);
			jo.put("Video", vid);
			jo.put("Lat",lat);
			jo.put("Lon",lon);
			jo.put("User", user);
			jo.put("Tags", tags);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	
		return jo;
	}
	
	public static JSONObject clientComment(String text, String user, int id)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("POST", 1);
			jo.put("Comment", text);
			jo.put("User", user);
			jo.put("ID", id);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	
		return jo;
	}
	//create json media messages to be sent to client
	public static JSONObject serverPictureToJson(String[] pic, String[] date, int[] id, String[][] tags, int[] rating)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("Picture", pic);
			jo.put("Date", date);
			jo.put("ID", id);
			jo.put("Rating", rating);
			jo.put("Tags", tags);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	
		return jo;
	}
	
	public static JSONObject serverVideoToJson(Object[] vid,String[] date, int[] id, int[] rating, String[][] tags)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("Video", vid);
			jo.put("Date", date);
			jo.put("ID", id);
			jo.put("Rating", rating);
			jo.put("Tags", tags);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	
		return jo;
	}
	public static JSONObject serverComments(String[] text, int id)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("Comment", text);
			jo.put("ID", id);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
	
		return jo;
	}
	//getter methods
	public static String[] clientGetImage(JSONObject jo)
	{
		try
		{
			JSONArray jsonArray = (JSONArray) jo.get("Picture");
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < jsonArray.length(); i++)
			{
				list.add(jsonArray.getString(i));
			}
			String[] arrayString = list.toArray(new String[list.size()]);
			return arrayString;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object getImage(JSONObject jo)
	{
		try {
			return jo.get("Picture");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getUser(JSONObject jo)
	{
		try {
			return (String)jo.get("User");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String[] clientGetUser(JSONObject jo)
	{
		try {
			return (String[])jo.get("User");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static Object getVideo(JSONObject jo)
	{
		try {
			return jo.get("Video");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static Object[] clientGetVideo(JSONObject jo)
	{
		try {
			return (Object[])jo.get("Video");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static int getID(JSONObject jo)
	{
		try {
			return jo.getInt("ID");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static Integer[] clientGetID(JSONObject jo)
	{	
		try
		{
			JSONArray jsonArray = (JSONArray) jo.get("ID");
			List<Integer> list = new ArrayList<Integer>();
			for(int i = 0; i < jsonArray.length(); i++)
			{
				list.add(jsonArray.getInt(i));
			}
			Integer[] arrayInt = list.toArray(new Integer[list.size()]);
			return arrayInt;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getDate(JSONObject jo)
	{
		try {
			return (String) jo.get("Date");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String[] clientGetDate(JSONObject jo)
	{
		try
		{
			JSONArray jsonArray = (JSONArray) jo.get("Date");
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < jsonArray.length(); i++)
			{
				list.add(jsonArray.getString(i));
			}
			String[] arrayString = list.toArray(new String[list.size()]);
			return arrayString;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String[] getTags(JSONObject jo)
	{
		try {
			return (String[]) jo.get("Tags");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String[][] clientGetTags(JSONObject jo)
	{
		try {
			return (String[][]) jo.get("Tags");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static int getRating(JSONObject jo)
	{
		try {
			return jo.getInt("Rating");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static Integer[] clientGetRating(JSONObject jo)
	{
		try
		{
			JSONArray jsonArray = (JSONArray) jo.get("Rating");
			List<Integer> list = new ArrayList<Integer>();
			for(int i = 0; i < jsonArray.length(); i++)
			{
				list.add(jsonArray.getInt(i));
			}
			Integer[] arrayInt = list.toArray(new Integer[list.size()]);
			return arrayInt;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getText(JSONObject jo)
	{	
		try {
			return (String) jo.get("Text");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static Double getLon(JSONObject jo)
	{	
		try {
			return jo.getDouble("Lon");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static Double[] clientGetLon(JSONObject jo)
	{	
		try {
			return (Double[])jo.get("Lon");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static Double getLat(JSONObject jo)
	{	
		try {
			return  jo.getDouble("Lat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static Double[] clientGetLat(JSONObject jo)
	{	
		try {
			return (Double[])jo.get("Lat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getComment(JSONObject jo)
	{	
		try {
			return (String) jo.get("Comment");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String[] getComments(JSONObject jo)
	{	
		try
		{
			JSONArray jsonArray = (JSONArray) jo.get("Comment");
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < jsonArray.length(); i++)
			{
				list.add(jsonArray.getString(i));
			}
			String[] arrayString = list.toArray(new String[list.size()]);
			return arrayString;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static int getVote(JSONObject jo)
	{
		try {
			return jo.getInt("Vote");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static int getPreviousVote(JSONObject jo)
	{
		try {
			return jo.getInt("Prev");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	public static String getFilter(JSONObject jo)
	{
		try {
			return (String) jo.get("Fliter");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	//boolean
	public static boolean isPic(JSONObject jo)
	{
		try
		{
			jo.get("Picture");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isVid(JSONObject jo)
	{
		try
		{
			jo.get("Video");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isText(JSONObject jo)
	{
		try
		{
			jo.get("Text");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isVote(JSONObject jo)
	{
		try
		{
			jo.get("Vote");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isUnVote(JSONObject jo)
	{
		try
		{
			jo.get("UnVote");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isFlag(JSONObject jo)
	{
		try
		{
			jo.get("Flag");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isUnFlag(JSONObject jo)
	{
		try
		{
			jo.get("UnFlag");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isComment(JSONObject jo)
	{
		try
		{
			jo.get("Comment");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isPostRequest(JSONObject jo)
	{
		try
		{
			jo.get("Bottom");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isPost(JSONObject jo)
	{
		try
		{
			jo.get("POST");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	public static boolean isGet(JSONObject jo)
	{
		try
		{
			jo.get("GET");
			return true;
		} catch(JSONException e) {
			return false;
		}
	}
	//voting messages and flags
	public static JSONObject vote(String User, int id, int prev, int vote)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("POST", 1);
			jo.put("Vote", vote);
			jo.put("ID", id);
			jo.put("Prev", prev);
			jo.put("User", User);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}
	
	/*Not in use. We do not let the user unvote
	public static JSONObject unVote(String User, int id, int prev)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("UnVote", "");
			jo.put("ID", id);
			jo.put("Prev", prev);
			jo.put("User", User);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}*/
	
	public static JSONObject flag(String User, int id)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("POST", 1);
			jo.put("Flag", "");
			jo.put("ID", id);
			jo.put("User", User);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}
	
	/* Not in use. We do not let the user unflag media content
	public static JSONObject unFlag(String User, int id)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("UnFlag", "");
			jo.put("ID", id);
			jo.put("User", User);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}*/
	
	//requests
	public static JSONObject getPosts(int bottomId, String filter, Double lat, Double lon, int sort)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("GET", 1);
			jo.put("Bottom", bottomId);
			jo.put("Lat",lat);
			jo.put("Lon",lon);
			jo.put("Filter", filter);
			jo.put("Sort", sort);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}
	
	public static int getSort(JSONObject jo) {
		try
		{
			return ((Integer) jo.get("Sort")).intValue();
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
}
