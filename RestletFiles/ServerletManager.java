package com.Simple;

import java.io.IOException;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class ServerletManager extends ServerResource implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Get("json")
	public JSONObject go(Representation r){
		return(text(r));
	}
	
	@Post("json")
	public JSONObject text(Representation r){ 
		
		JSONObject obj = parseJSON(r);
		JSONObject ret = new JSONObject();
		
		if(JSONMessage.isGet(obj)){
			GetManager getManager = new GetManager(obj);
			ret = getManager.sqlSelect(obj);
		}
		else if(JSONMessage.isPost(obj)){
			PostManager postManager = new PostManager(obj);
			ret = postManager.sqlInsertInto(obj);
		}
		else{
			System.err.println("Did not recognize Message Type.");
		}
		
		return ret;
	}
	
	private JSONObject parseJSON(Representation r){
		JSONObject obj = null;
		try {
			obj = new JSONObject(r.getText());
		} catch (JSONException | IOException e) {
			System.err.println("Failed in converting Representation to JSONObject");
		}
		return obj;
	}
}
