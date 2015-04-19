package com.frame.app.tasks;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.frame.app.Core.JSONMessage;

import android.os.AsyncTask;

public class PostCommentTask extends AsyncTask<Object, Void, Void> 
{

	@Override
	protected Void doInBackground(Object... params) 
	{		
		ClientResource res = new ClientResource(
				params[0].toString());
		res.setMethod(Method.POST);

		
		String text = (String)params[1]; //This is the user
		String user = (String)params[2]; //This is the id
		Integer Id = (Integer) params[3]; //This is the value of the vote +1 = upvote, -1 = downvote
		
		JSONObject obj = JSONMessage.commentToJson(Id, text, user);
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