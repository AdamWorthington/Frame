package com.frame.app.Core;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.frame.app.Core.JSONMessage;

import android.os.AsyncTask;

public class PostPictureTask extends AsyncTask<Object, Void, Void> 
{

	@Override
	protected Void doInBackground(Object... params) 
	{		
		ClientResource res = new ClientResource(
				params[0].toString());
		res.setMethod(Method.POST);
		
		
		String date = (String) params[2];
		int id = (Integer) params[3];
		String[] args = (String[]) params[4];
		int count = (Integer) params[5];

		JSONObject obj = JSONMessage.serverPictureToJson(params[1], date, id, args, count);
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			res.post(stringRep).write(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	} 
}