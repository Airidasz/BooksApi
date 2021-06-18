package com.airidasz.booksapi;

import java.math.BigDecimal;
import java.time.Year;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AntiqueBook extends Book {
	@JsonProperty("releaseYear")
	protected Integer releaseYear;
	
	public AntiqueBook() {
        super();
    }
	
	public AntiqueBook(String name, String author, String barcode, Integer quantity, Double price, Integer releaseYear) {
		super(name, author, barcode, quantity, price);
		
		if (releaseYear > 1900) {
			throw new IllegalArgumentException("Release year cannot be greater that 1900");
		} 

		this.releaseYear = releaseYear;
	}
	
	@JsonIgnore
	@Override
	public BigDecimal getTotalPrice() {
		int currentYear = Year.now().getValue();
		BigDecimal yearsBetween = BigDecimal.valueOf((currentYear- this.releaseYear) / 10.0);
		return this.price.multiply(BigDecimal.valueOf(this.quantity)).multiply(yearsBetween);
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(Integer releaseYear) {
		if (releaseYear > 1900) {
			throw new IllegalArgumentException("Release year cannot be greater that 1900");
		} 
		
		this.releaseYear = releaseYear;
	}
	
	@Override
	public boolean hasNullValues() {
		return (super.hasNullValues() || releaseYear == null);
	}
	
	public void update(AntiqueBook book) {
		super.update(book);
		if(book.releaseYear != null) setReleaseYear(book.releaseYear);
	}
}
