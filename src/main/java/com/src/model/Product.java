package com.src.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;

/**
 * @author Mihnea
 * This class models the Product entity. We consider the Product to have a 
 * name and a price. A particular Product can only appear in one given Store,
 * since it holds data unique to that Store(i.e. the price of the Product, which
 * varies across Stores). The Product can also appear on Orders, and we will also
 * persist this information regarding where and when the Products were sold, in the
 * shape of a relationship with the Order entity.
 * Same applies for the Sale entity, which will extend this class.
 *
 *
 */
@Entity // This tells Hibernate to make a table named "product" out of this class
@Inheritance(strategy = InheritanceType.JOINED)//This tells Hibernate that this class will be extended
@Table(name="product") 
public class Product {
	
	

	@Id//Id is generated automatically and so requires no setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long productId;
	
	//Name of the product
	@Column(name = "name")
	private String name;
	
	//Price of the product
	@Column(name = "price")
	private Float price;
	
	//This product appears in only one store, and a store has many products => Many to One relationship between
	//Product and Store
	@ManyToOne
    @JoinColumn(name ="FK_StoreId")
    private Store store;
	
	//A product can appear on multiple orders (the notion of product quantity is not treated in this problem),
	//and an order can have multiple products => Many to Many relationship between Product and Order
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="products")
    private Set <Order> orders = new HashSet <Order> ();

	//Getters and setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Long getProductId() {
		return productId;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}
}
