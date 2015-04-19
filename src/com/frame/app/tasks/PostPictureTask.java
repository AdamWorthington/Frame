package com.frame.app.tasks;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import com.frame.app.Core.JSONMessage;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class PostPictureTask extends AsyncTask<Object, Void, Bitmap> 
{

	@Override
	protected Bitmap doInBackground(Object... params) 
	{		
		ClientResource res = new ClientResource(
				params[0].toString());
		res.setMethod(Method.POST);
		
		Bitmap picture = (Bitmap) params[1];
		Double latitude = (Double) params[2];
		Double longitude = (Double) params[3];
		String user = (String) params[4];
		String[] tags = (String[]) params[5];

		
		JSONObject obj = JSONMessage.clientPictureToJson(picture, latitude.doubleValue(), longitude.doubleValue(), user, tags, false);
		StringRepresentation stringRep = new StringRepresentation(
				obj.toString());
		stringRep.setMediaType(MediaType.APPLICATION_JSON);

		try {
			res.post(stringRep).write(System.out);
			Log.d("POSTPICTURE", "SENT");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return picture;
	}
	
    protected void onPostExecute(Bitmap result) 
    {
    	result.recycle();
	}
}