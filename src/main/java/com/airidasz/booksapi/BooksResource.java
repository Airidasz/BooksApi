package com.airidasz.booksapi;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/books")
public class BooksResource {
	private BookDAO books = BookDAO.getInstance();

	// Lists books grouped by quantity
	// Data is sorted by total price
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
    		status = Response.Status.NOT_FOUND;
    	
    	return Response
	    	      .status(status)
	    	      .entity(booksByQuantity)
	    	      .build();
    }
    
    // Adds books to DAO instance
 	// Books are of three types
    // the type of book is selected by looking for certain fields
    // in request
    @POST
    @Produces("application/json")
    public Response addBook(String request){
    	Book book = null;
    	try {
    		ObjectMapper objectMapper = new ObjectMapper();
    		JsonNode jsonNode = objectMapper.readTree(request);
			
    		// Mapping class chosen by field
    		if (jsonNode.get("scienceIndex") != null)
    			book = objectMapper.readValue(request, ScientificJournal.class);
    		else if(jsonNode.get("releaseYear") != null)
    			book = objectMapper.readValue(request, AntiqueBook.class);	
			else
				book = objectMapper.readValue(request, Book.class);	
    		
    		// Null value check
    		if(book.hasNullValues())
    			return Response.status(Response.Status.BAD_REQUEST)
        				.entity("{ \"error\": \"Required fields missing\"}")
        				.build();
    		
    		// Check for book with same barcode
    		if(books.get(book.getBarcode()) != null)
        		return Response.status(Response.Status.BAD_REQUEST)
        				.entity("{ \"error\": \"Book with same barcode exists\"}")
        				.build();
				
	    	books.add(book);

		} catch (JsonMappingException e) {
			// This triggers when there are extra fields in request or when setting scienceIndex or releaseYear 
			return Response.status(Response.Status.BAD_REQUEST).entity("{ \"error\": \"Bad format\"}").build();
		} catch (JsonProcessingException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{}").build();
		}
    	
    	return Response.status(Response.Status.CREATED).build();
    }
    
    // Get total price of book
    // total price calculations depend on book type
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
    
    // Find book using its barcode
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
    
    // Update chosen book
    @PUT
    @Produces("application/json")
    @Path("/{barcode}")
    public Response updateBook(@PathParam("barcode") String barcode, String request) {
    	Book book = books.get(barcode);
    	
    	if(book == null)
    		return Response.status(Response.Status.NOT_FOUND)
    				.entity("{ \"error\": \"Book not found\"}")
    				.build();
    		
    	
    	Book updatedBook = null;
    	ObjectMapper objectMapper = new ObjectMapper();
    	try {
    		updatedBook = objectMapper.readValue(request, book.getClass());	
    		JsonNode jsonNode = objectMapper.readTree(request);
    		
    		// Check for barcode changes
        	if(jsonNode.get("barcode") != null) {
        		if(updatedBook.getBarcode() != barcode && books.get(updatedBook.getBarcode()) != null) {
        			return Response.status(Response.Status.BAD_REQUEST)
        					.entity("{ \"error\": \"Updated barcode is used by different book\"}")
        					.build();
        		}
        	}
		} catch (JsonMappingException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("{ \"error\": \"Bad format\"}").build();
		} catch (JsonProcessingException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{}").build();
		}
    	
    	books.update(barcode, updatedBook);
    	
        return Response
        	      .status(Response.Status.OK)
        	      .build();
    }
    
   
}
