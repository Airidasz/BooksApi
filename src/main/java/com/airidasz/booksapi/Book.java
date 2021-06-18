package com.airidasz.booksapi;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book {
	@JsonProperty("name")
	protected String name;
	
	@JsonProperty("author")
	protected String author;

    @JsonProperty("barcode")
	protected String barcode;
	
	@JsonProperty("quantity")
	protected Integer quantity;
	
	@JsonProperty("price")
	protected BigDecimal price; // Price per unit
	
	public Book() {
        super();
    }
	
	public Book(String name, String author, String barcode, Integer quantity, Double price) {
		this.name = name;
		this.author = author;
		this.barcode = barcode;
		this.quantity = quantity;
		this.price = BigDecimal.valueOf(price);
	}
	
	@JsonIgnore
	public BigDecimal getTotalPrice()  {
		return this.price.multiply(BigDecimal.valueOf(this.quantity));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public void update(Book book) {
		if(book.name != null) setName(book.name);
		if(book.author != null) setAuthor(book.author);
		if(book.barcode != null) setBarcode(book.barcode);
		if(book.quantity != null) setQuantity(book.quantity);
		if(book.price != null) setPrice(book.price);
	}
	
	public boolean hasNullValues() {
		return (name == null || author == null || barcode == null || quantity == null || price == null);
	}
}
