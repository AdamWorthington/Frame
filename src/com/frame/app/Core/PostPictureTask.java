package com.frame.app.Core;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import android.os.AsyncTask;

public class PostPictureTask extends AsyncTask<Object, Void, Void> 
{

	@Override
	protected Void doInBackground(Object... params) 
	{		
		ClientResource res = new ClientResource(
				params[0].toString());
		res.setMethod(Method.POST);

		JSONObject obj = textToJson(params[1].toString(), 500.43, 235.12, "Adam",
				"10/10/2015", new String[]{ "1"});
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			res.post(stringRep).write(System.out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("----------------" + e.toString()
					+ "----------------");
			e.printStackTrace();
		}
		return null;
	} 
	
	private static JSONObject textToJson(String text, Double lat, Double lon,
			String user, String date, String[] tags) {
		JSONObject jo = new JSONObject();

		try {
			jo.put("Text", text);
			jo.put("Lat", lat);
			jo.put("Lon", lon);
			jo.put("User", user);
			jo.put("Date", date);
			jo.put("Tags", tags);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jo;
	}
}