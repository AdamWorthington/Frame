package com.Simple;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;




/**
 * Resource which has only one representation.
 *
 */
public class SimpleServlet extends ServerResource {

    @Get
    public String represent() {
        return "hello, world (from the cloud!)";
    }

    @Post("json") Representation acceptAndReturnJson(Representation entity) {
    	try {
			System.out.println(entity.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
        // ...
    }
    
}