package com.src.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Mihnea
 * This class models the Order entity. We consider the Order to have a name, an order
 * placement date, a total sum (i.e. the sum of the prices of the ordered items), a List of
 * Products that were purchased, and a List of Stores that have to be visited in order to purchase
 * the Products.
 * It should be noted that while the application is trying to find products on Sale 
 * to be purchased, these are ultimately peristed as Products in the Order. This choice was made
 * simply because the extra information provided by the Sale class, such as the Sale date interval,
 * is no longer of interest after the purchase, and so the Product class is simpler.
 */
@Entity // This tells Hibernate to make a table named "customerOrder" out of this class
//Please notice the slight difference between the name of the table and the name of the entity
//This happens because "order" is an SQL keyword and so naming the table "order" would not be possible
@Table(name="customerOrder")
public class Order {

	@Id//Id is generated automatically and so requires no setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long orderId;
	
	//The name of the client that issued the Order
	@Column(name = "name")
	private String name;
	
	//An Order can have multiple Products, and a Product can appear on multiple Orders(disregarding the notion
	//of quantity) => Many to Many relationship between Order and Product
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Order_products", 
        joinColumns = { @JoinColumn(name = "orderId") }, 
        inverseJoinColumns = { @JoinColumn(name = "product_id") }
    )
    private Set <Product> products = new HashSet <Product> ();
	
	//An Order can have multiple Stores, and a Store can appear on multiple Orders => Many to Many relationship
	//between Order and Store
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Order_stores", 
        joinColumns = { @JoinColumn(name = "orderId") }, 
        inverseJoinColumns = { @JoinColumn(name = "storeId") }
    )
    private Map <Integer, Store> stores = new HashMap <Integer, Store> ();
	
	//Date of the Order
	@Column(name = "date")
	private Date date;
	
	//The Order's requirements: They are not persisted as Product entities, since they do not hold any
	//other information besides their names, and it makes more sense here to simply use a String.
	//Only the final Products (the ones that are actually found in stores and hold information) are
	//persisted as Product entities
	@Column(name = "requirements")
	private String requirements;
	
	//The sum of prices of the Order
	@Column(name = "sum")
	private Float sum;
	
	//Getters and setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void addProduct(Product product) {
		this.products.add(product);
	}

	public Long getOrderId() {
		return orderId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public Float getSum() {
		return sum;
	}

	public void setSum(Float sum) {
		this.sum = sum;
	}

	public Map<Integer, Store> getStores() {
		return stores;
	}

	public void addStore(Store store, Integer priority) {
		this.stores.put(priority, store);
	}

	public void setStores(Map<Integer, Store> stores) {
		this.stores = stores;
	}
	
}