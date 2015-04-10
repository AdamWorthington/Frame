package com.Simple;


import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessage {
	
	//create json media messages to be sent to client
	public static JSONObject clientPictureToJson(Object pic, Double lat, Double lon, String user, String[] tags)
	{
		JSONObject jo = new JSONObject();
		try
		{
			jo.put("Picture", pic);
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
	
	public static JSONObject clientTextToJson(String text, Double lat, Double lon, String user,String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("Text", text);
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
	public static JSONObject serverPictureToJson(Object pic, String date, int id, String[] tags, int rating)
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
	
	public static JSONObject serverVideoToJson(Object vid,String date, int id, int rating, String[] tags)
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
	public static Object getImage(JSONObject jo)
	{
		try {
			return jo.get("Picture");
		} catch (JSONException e) {
			//e.printStackTrace();
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
	public static int getID(JSONObject jo)
	{
		try {
			return jo.getInt("ID");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
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
	public static String[] getTags(JSONObject jo)
	{
		try {
			return (String[]) jo.get("Tags");
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
	public static String getText(JSONObject jo)
	{	
		try {
			return (String) jo.get("Text");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String getUser(JSONObject jo)
	{	
		try {
			return (String) jo.get("User");
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
	public static Double getLat(JSONObject jo)
	{	
		try {
			return  jo.getDouble("Lat");
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
		try {
			return (String[]) jo.get("Comment");
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
	public static boolean isComments(JSONObject jo)
	{
		try
		{
			jo.get("Comments");
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
	public static boolean isCommentRequest(JSONObject jo)
	{
		try
		{
			jo.get("requestComments");
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
	}
	public static JSONObject flag(String User, int id)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
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
	}
	//requests
	public static JSONObject getPosts(int bottomId, String filter, Double lat, Double lon)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("Bottom", bottomId);
			jo.put("Lat",lat);
			jo.put("Lon",lon);
			jo.put("Filter", filter);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}
	public static JSONObject requestComments(int Id)
	{
		JSONObject jo = new JSONObject();
		
		try
		{
			jo.put("ID", Id);
			jo.put("requestComments", Id);
		}
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return jo;
	}
}
