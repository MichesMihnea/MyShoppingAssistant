package com.src.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Mihnea
 * This class models the Store entity. The Store has a name, and a boolean value informing us whether
 * the Store is open on Sundays or not. The Store can have multiple Products or Sales in it, and it
 * can also appear on multiple Orders. The Store also keeps track of the purchases performed here, in
 * the form of a relationship with the Order entity.
 */
@Entity // This tells Hibernate to make a table named "store" out of this class
@Table(name="store")
public class Store {

	@Id//Id is generated automatically and so requires no setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long storeId;
	
	//The name of the Store
	@Column(name = "name")
	private String name;
	
	//Is the Store open on Sundays?
	@Column(name = "openOnSunday")
	private Boolean openOnSunday;
	
	//The Store can have multiple Products, but the Products only appear in one given
	//Store => One to Many relationship between Store and Product
	@OneToMany(mappedBy="store", cascade=CascadeType.ALL)
    private Set <Product> products = new HashSet <Product> ();
	
	//The Store can have multiple Sales, but the Sales only appear in one given
		//Store => One to Many relationship between Store and Sale
	@OneToMany(mappedBy="store", cascade=CascadeType.ALL)
    private Set <Sale> sales = new HashSet <Sale> ();
	
	//The Store can appear on multiple Orders, and an Order can have multiple Stores
	// => Many to Many relationship between Store and Order
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="stores")
    private Set <Order> orders = new HashSet <Order> ();

	//Getters and setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpenOnSunday() {
		return openOnSunday;
	}

	public void setOpenOnSunday(Boolean openOnSunday) {
		this.openOnSunday = openOnSunday;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void addProduct(Product product) {
		this.products.add(product);
	}

	public Set<Sale> getSale() {
		return sales;
	}

	public void addSale(Sale sale) {
		this.sales.add(sale);
	}

	public Long getStoreId() {
		return storeId;
	}
	
	public Set<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		this.orders.add(order);
	}
}
