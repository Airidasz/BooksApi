package com.airidasz.booksapi;

import java.util.ArrayList;
import java.util.List;

public class BookDAO {
	private static BookDAO instance;
	private static List<Book> books = new ArrayList<>();

    static {
    	books.add(new Book("Book1", "Author1", "1", 8, 20.0));
    	books.add(new AntiqueBook("Book2", "Author2", "2", 4, 50.0, 1500));
    	books.add(new Book("Book3", "author3", "3", 2, 58.0));
    	books.add(new ScientificJournal("Book4", "Author4", "4", 4, 580.0, 2));
    }
    
    private BookDAO() {
        
    }
     
    public static BookDAO getInstance() {
        if (instance == null) {
            instance = new BookDAO();
        }
         
        return instance;               
    }
     
    public List<Book> listAll() {
        return new ArrayList<Book>(books);
    }
     
    public void add(Book book) {
    	books.add(book);
    }
     
    public Book get(String barcode) {
        return books.stream()
    			.filter(e -> barcode.equals(e.getBarcode()))
    			.findAny().orElse(null);
    }
     
    public void update(String barcode, Book book) {
    	get(barcode).update(book);
    }
}
