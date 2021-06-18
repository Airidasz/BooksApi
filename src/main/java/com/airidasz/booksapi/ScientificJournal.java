package com.airidasz.booksapi;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ScientificJournal extends Book {
	@JsonProperty("scienceIndex")
	protected Integer scienceIndex;
	
	public ScientificJournal() {
        super();
    }
	
	public ScientificJournal(String name, String author, String barcode, Integer quantity, Double price, Integer scienceIndex) {
		super(name, author, barcode, quantity, price);
		
		if (scienceIndex < 1 || scienceIndex > 10) {
			throw new IllegalArgumentException("Science index can only be between 1 and 10");
		}
		this.scienceIndex = scienceIndex;
	}

	public Integer getScienceIndex() {
		return scienceIndex;
	}

	public void setScienceIndex(Integer scienceIndex) {
		if (scienceIndex < 1 || scienceIndex > 10) {
			throw new IllegalArgumentException("Science index can only be between 1 and 10");
		} 

		this.scienceIndex = scienceIndex;
	}
	
	@JsonIgnore
	@Override
	public BigDecimal getTotalPrice() {
		return this.price.multiply(BigDecimal.valueOf(this.quantity)).multiply(BigDecimal.valueOf(scienceIndex));
	}
	
	@Override
	public boolean hasNullValues() {
		return (super.hasNullValues() || scienceIndex == null);
	}

	public void update(ScientificJournal book) {
		super.update(book);
		if(book.scienceIndex != null) setScienceIndex(book.scienceIndex);
	}
}
