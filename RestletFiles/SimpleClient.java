package com.Test;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class SimpleClient {
	public static void main(String[] args) {
		getText();
	}
	
	public static void getText(){
		ClientResource res = new ClientResource("http://1-dot-august-clover-86805.appspot.com/Simple");
		res.setMethod(Method.GET);
		JSONObject obj = textToJson("Daniel", 500.43, 235.12, "Adam", "10/10/2015", null);
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		try {
			res.post(stringRep).write(System.out);
		} catch (ResourceException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void postText(){
		ClientResource res = new ClientResource("http://1-dot-august-clover-86805.appspot.com/Simple");
		res.setMethod(Method.POST);
		//res.getReference().addQueryParameter("format", "json");

		JSONObject obj = textToJson("Craig!!!", 500.43, 235.12, "Adam", "10/10/2015", null);
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			res.post(stringRep).write(System.out);
		} catch (ResourceException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static JSONObject textToJson(String text, Double lat, Double lon, String user, String date, String[] tags)
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
}
