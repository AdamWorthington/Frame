package com.Simple;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class SimpleServlet extends ServerResource {

	@Get
	public String represent() {
		return "hello, world (from the cloud!)";
	}

	@Post("json")
	public String createBank(Representation r) throws IOException {
		String s = r.getText();
		System.out.println(s);
		//s = s.substring(1, s.length() - 1); //Takes off single quotes
		JSONObject obj = null;
		
		try {
			obj = new JSONObject(s);
		} catch (JSONException e1) {
			System.err.println("Couldn't make JSONObject from string: " + s);
		}
		
		String value = "";
		
		try {
			value = obj.getString("password");
		} catch (JSONException e) {
			System.err.println("Couldn't find key: " + "password");
		}

		return value;
	}
}
