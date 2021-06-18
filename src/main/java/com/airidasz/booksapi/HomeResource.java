package com.airidasz.booksapi;

import java.io.File;
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
    	String indexFileLocation = context.getRealPath("/") +"\\index.html";
    	File indexFile = new File(indexFileLocation);
    	return Response.ok(indexFile).build();
    }
   
}