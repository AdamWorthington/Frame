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
		ClientResource res = new ClientResource("http://localhost:8888/Simple");
		res.setMethod(Method.POST);
		//res.getReference().addQueryParameter("format", "json");

		JSONObject obj = new JSONObject();
		try {
			obj.put("username", "adam");
			obj.put("password", "xyz");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
}
