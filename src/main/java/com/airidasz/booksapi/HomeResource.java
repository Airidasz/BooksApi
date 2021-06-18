package com.airidasz.booksapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public class HomeResource {
    @GET
    @Produces("application/json")
    public Response index() {    	
        return Response
	    	      .status(Response.Status.OK)
	    	      .entity("home")
	    	      .build();
    }
}