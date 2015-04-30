package com.Simple;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class ServerRouter extends Application {
	public ServerRouter(){
		super();
	}
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
    	
        Router router = new Router(getContext());

        router.attach("/Get", ServerletManager.class);
        router.attach("/Post", ServerletManager.class);
        router.attach("/Delete", ServerletManager.class);

        return router;
    }
}