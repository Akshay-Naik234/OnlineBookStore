package com.springboot.bookStore.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "book_name")
	private String name;
	
	@Column(name = "book_description")
	private String description;
	
	@Lob
	@Column(name = "image_url",columnDefinition = "MEDIUMBLOB")
	private String image;
	
	@Column(name = "book_title")
	private String title;
	
	@Column(name = "book_category")
	private String Category;
	
	@Column(name="available_quantity")
	private int availableQuantity;
	
	@Column(name = "book_price")
	private double price;

}
