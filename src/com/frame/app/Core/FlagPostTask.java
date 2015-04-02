package com.frame.app.Core;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import android.os.AsyncTask;

public class FlagPostTask extends AsyncTask<Object, Void, Void> 
{

	@Override
	protected Void doInBackground(Object... params) 
	{		
		ClientResource res = new ClientResource(
				params[0].toString());
		res.setMethod(Method.POST);

		
		String user = (String)params[1]; //This is the user
		Integer Id = (Integer)params[2]; //This is the id
		
		JSONObject obj = JSONMessage.flag(user, Id.intValue());
		
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			//res.post(stringRep).write(System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	} 
}