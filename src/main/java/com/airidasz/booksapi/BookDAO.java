package com.airidasz.booksapi;

import java.util.ArrayList;
import java.util.List;

public class BookDAO {
	private static BookDAO instance;
	private static List<Book> books = new ArrayList<>();

    static {
    	books.add(new AntiqueBook("Knyga4", "Airidas", "1", 8, 580.0, 14141));
    	books.add(new AntiqueBook("Knyga2", "Airidas", "2", 4, 50.0, 1999));
    	books.add(new AntiqueBook("Knyga4", "Airidas", "3", 2, 580.0, 1999));
    	books.add(new ScientificJournal("Knyga4", "Airidas", "4", 4, 580.0, 1999));
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
     
    /*public boolean delete(int id) {
        Product productToFind = new Product(id);
        int index = data.indexOf(productToFind);
        if (index >= 0) {
            data.remove(index);
            return true;
        }
         
        return false;
    }*/
     
    public void update(Book book) {
    	get(book.getBarcode()).update(book);
    }
}
