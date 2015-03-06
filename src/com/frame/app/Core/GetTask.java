package com.frame.app.Core;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import android.os.AsyncTask;

public class GetTask extends AsyncTask<Object, Void, JSONObject> 
{

	@Override
	protected JSONObject doInBackground(Object... params) 
	{		
		ClientResource res = new ClientResource(params[0].toString());
		res.setMethod(Method.GET);
		JSONObject obj = textToJson("", 0.0, 0.0, "", "", new String[]{""});
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);
		JSONObject o = null;
		try {
			Representation r = res.post(stringRep);
			o = new JSONObject(r.getText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) 
	{
	    super.onPostExecute(result);
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