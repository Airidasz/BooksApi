package com.airidasz.booksapi;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("books")
public class BooksResource {
	private BookDAO books = BookDAO.getInstance();

    @GET
    @Produces("application/json")
    public Response getBooks() {    	
    	TreeMap<Integer, List<Book>> booksByQuantity = new TreeMap<Integer, List<Book>>();
    	
    	List<Book> sortedBooks = books.listAll().stream()
    			.sorted((p1, p2)-> p1.getTotalPrice().compareTo(p2.getTotalPrice()))
    			.collect(Collectors.toList());
    	
    	for(Book book : sortedBooks) {
    		booksByQuantity.computeIfAbsent(book.getQuantity(), k -> new ArrayList<>()).add(book);
    	}
    	
    	Status status = Response.Status.OK;
    	if(sortedBooks.size() <= 0)
    		status = Response.Status.NO_CONTENT;
    	
        return Response
	    	      .status(status)
	    	      .entity(booksByQuantity)
	    	      .build();
    }
    
    @GET
    @Produces("application/json")
    @Path("/{barcode}/price")
    public Response getTotalPrice(@PathParam("barcode") String barcode) {
    	Book book = books.get(barcode);
    	
    	if(book == null)
    		return Response.status(Response.Status.NOT_FOUND)
    				.entity("{ \"error\": \"Book not found\"}")
    				.build();
    	
        return Response
        	      .status(Response.Status.OK)
        	      .entity(String.format("{ \"price\": %.2f }",  book.getTotalPrice()))
        	      .build();
    }
    
    @GET
    @Produces("application/json")
    @Path("/{barcode}")
    public Response getBook(@PathParam("barcode") String barcode) {
    	Book book = books.get(barcode);
    	
    	if(book == null)
    		return Response.status(Response.Status.NOT_FOUND)
    				.entity("{ \"error\": \"Book not found\"}")
    				.build();
    	
        return Response
        	      .status(Response.Status.OK)
        	      .entity(book)
        	      .build();
    }
    
    @POST
    @Produces("application/json")
    @Path("/{barcode}/update")
    public Response updateBook(@PathParam("barcode") String barcode, String request) {
    	Book book = books.get(barcode);
    	
    	if(book == null)
    		return Response.status(Response.Status.NOT_FOUND)
    				.entity("{ \"error\": \"Book not found\"}")
    				.build();
    		
    	
    	Book updatedBook = null;
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
    		updatedBook = objectMapper.readValue(request, book.getClass());	
		} catch (JsonMappingException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("{ \"error\": \"Bad format\"}").build();
		} catch (JsonProcessingException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{}").build();
		}
    	updatedBook.setBarcode(book.getBarcode());
    	books.update(updatedBook);
    	
    	book = books.get(book.getBarcode());
        return Response
        	      .status(Response.Status.OK)
        	      .entity(book)
        	      .build();
    }
    
    
    @POST
    @Produces("application/json")
    public Response addBook(String request){
    	Book book = null;
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
    		JsonNode jsonNode = objectMapper.readTree(request);
			
    		if (jsonNode.get("scienceIndex") != null)
    			book = objectMapper.readValue(request, ScientificJournal.class);
    		else if(jsonNode.get("releaseYear") != null)
    			book = objectMapper.readValue(request, AntiqueBook.class);	
			else
				book = objectMapper.readValue(request, Book.class);	
    		
    		if(book.hasNullValues())
    			return Response.status(Response.Status.BAD_REQUEST)
        				.entity("{ \"error\": \"Required fields missing\"}")
        				.build();
    		
    		if(books.get(book.getBarcode()) != null)
        		return Response.status(Response.Status.BAD_REQUEST)
        				.entity("{ \"error\": \"Book with same barcode exists\"}")
        				.build();
				
	    	books.add(book);

		} catch (JsonMappingException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("{ \"error\": \"Bad format\"}").build();
		} catch (JsonProcessingException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{}").build();
		}
    	
    	return Response.status(Response.Status.CREATED).entity(book).build();
    }
}
