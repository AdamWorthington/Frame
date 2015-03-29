/*package JSON;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class JSONMessage {
	
	//create json media messages to be sent to client
	public static JSONObject clientPictureToJson(Object pic, Double lat, Double lon, String user, String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Picture", pic);
		jo.put("Lat",lat);
		jo.put("Lon",lon);
		jo.put("User", user);
		jo.put("Tags", tags);
		
		return jo;
	}
	
	public static JSONObject clientTextToJson(String text, Double lat, Double lon, String user,String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Text", text);
		jo.put("Lat",lat);
		jo.put("Lon",lon);
		jo.put("User", user);
		jo.put("Tags", tags);
		
		return jo;
	}
	
	public static JSONObject clientVideoToJson(Object vid, Double lat, Double lon, String user, String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Video", vid);
		jo.put("Lat",lat);
		jo.put("Lon",lon);
		jo.put("User", user);
		jo.put("Tags", tags);
		
		return jo;
	}
	public static JSONObject clientComment(String text, String user, int id)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Comment", text);
		jo.put("User", user);
		jo.put("ID", id);
		
		return jo;
	}
	//create json media messages to be sent to client
	public static JSONObject serverPictureToJson(Object pic, String date, int id, String[] tags, int rating)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Picture", pic);
		jo.put("Date", date);
		jo.put("ID", id);
		jo.put("Rating", rating);
		jo.put("Tags", tags);
		
		return jo;
	}
	
	public static JSONObject serverVideoToJson(Object vid,String date, int id, int rating, String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Video", vid);
		jo.put("Date", date);
		jo.put("ID", id);
		jo.put("Rating", rating);
		jo.put("Tags", tags);
		
		return jo;
	}
	public static JSONObject serverComments(String[] text, int id)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Comment", text);
		jo.put("ID", id);
		
		return jo;
	}
	//getter methods
	public static Object getImage(JSONObject jo)
	{
		return jo.get("Picture");
	}
	public static Object getVideo(JSONObject jo)
	{
		return jo.get("Video");
	}
	public static int getID(JSONObject jo)
	{
		return jo.getInt("ID");
	}
	public static String getDate(JSONObject jo)
	{
		return (String) jo.get("Date");
	}
	public static String[] getTags(JSONObject jo)
	{
		return (String[]) jo.get("Tags");
	}
	public static int getRating(JSONObject jo)
	{
		return jo.getInt("Rating");
	}
	public static String getText(JSONObject jo)
	{	
		return (String) jo.get("Text");
	}
	public static Double getLon(JSONObject jo)
	{	
		return jo.getDouble("Lon");
	}
	public static Double getLat(JSONObject jo)
	{	
		return  jo.getDouble("Lat");
	}
	public static String getComment(JSONObject jo)
	{	
		return (String) jo.get("Comment");
	}
	public static String[] getComments(JSONObject jo)
	{	
		return (String[]) jo.get("Comment");
	}
	public static int getVote(JSONObject jo)
	{
		return jo.getInt("Vote");
	}
	public static int getPreviousVote(JSONObject jo)
	{
		return jo.getInt("Prev");
	}
	public static String getFilter(JSONObject jo)
	{
		return (String) jo.get("Fliter");
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
	
	//voting messages and flags
	public static JSONObject vote(String User, int id, int prev, int vote)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Vote", vote);
		jo.put("ID", id);
		jo.put("Prev", prev);
		jo.put("User", User);
		
		return jo;
	}
	public static JSONObject unVote(String User, int id, int prev)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("UnVote", "");
		jo.put("ID", id);
		jo.put("Prev", prev);
		jo.put("User", User);
		
		return jo;
	}
	public static JSONObject flag(String User, int id)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Flag", "");
		jo.put("ID", id);
		jo.put("User", User);
		
		return jo;
	}
	public static JSONObject unFlag(String User, int id)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("UnFlag", "");
		jo.put("ID", id);
		jo.put("User", User);
		
		return jo;
	}
	//requests
	public static JSONObject getPosts(int bottomId, String filter, Double lat, Double lon)
	{
		JSONObject jo = new JSONObject();
		
		jo.put("Bottom", bottomId);
		jo.put("Lat",lat);
		jo.put("Lon",lon);
		jo.put("Filter", filter);
		
		return jo;
	}
}*/
