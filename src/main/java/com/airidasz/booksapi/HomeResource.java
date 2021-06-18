package com.airidasz.booksapi;

import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class HomeResource {
	@javax.ws.rs.core.Context 
	ServletContext context;
	
    @GET
    public Response index() {  
    	InputStream indexFile = context.getResourceAsStream("index.html");
    	return Response.ok(indexFile).build();
    }
   
}