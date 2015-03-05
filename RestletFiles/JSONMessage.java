package com.Test;

import java.io.ByteArrayOutputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMessage {
	
	public static JSONObject pictureToJson(String pic, Double lat, Double lon, String user, String date, String[] tags)
	{
		JSONObject jo = new JSONObject();
		//image to string
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//ImageIO.write( pic, "jpg", baos );
		try {
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		String image = new String(imageInByte);
		
			jo.put("Picture", image);
			jo.put("Lat", lat);
			jo.put("Lon", lon);
			jo.put("User", user);
			jo.put("Date", date);
			jo.put("Tags", tags);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return jo;
	}
	/*
	public static BufferedImage jsonToImage(JSONObject jo) throws IOException
	{
		BufferedImage image;
		String imageInBytes = (String) jo.get("Picture");
		byte[] bytes = imageInBytes.getBytes();
		InputStream in = new ByteArrayInputStream(bytes);
		image = ImageIO.read(in);
		in.close();
		
		return image;
	}
	*/
	public static JSONObject TextToJson(String text, Double lat, Double lon, String user, String date, String[] tags)
	{
		JSONObject jo = new JSONObject();
		
		try {
			jo.put("Text", text);
			jo.put("Lat",lat);
			jo.put("Lon",lon);
			jo.put("User", user);
			jo.put("Date", date);
			jo.put("Tags", tags);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jo;
	}
	public static String getID(JSONObject jo) throws JSONException
	{
		return (String) jo.get("ID");
	}
	public static String getDate(JSONObject jo)throws JSONException
	{
		return (String) jo.get("Date");
	}
	public static String[] getTags(JSONObject jo)throws JSONException
	{
		return (String[]) jo.get("Tags");
	}
	public static int getRating(JSONObject jo)throws JSONException
	{
		return jo.getInt("Rating");
	}
	public static String getText(JSONObject jo)throws JSONException
	{	
		return (String ) jo.get("Text");
	}
}
