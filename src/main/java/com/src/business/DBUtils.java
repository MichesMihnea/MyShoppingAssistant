package com.src.business;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import com.src.model.Order;
import com.src.model.Product;
import com.src.model.Sale;
import com.src.model.Store;
import com.src.repos.OrderRepository;
import com.src.repos.ProductRepository;
import com.src.repos.SaleRepository;
import com.src.repos.StoreRepository;

/**
 * @author Mihnea
 * A utility class to handle the database operations. The database can only be accessed to this class
 */
@Repository//This tells Spring that this Interface is a repository, and so it can
//find it when looking for dependencies.
public class DBUtils {

	@Autowired//This tells Spring to inject a dependency here
	StoreRepository storeRepository;
	
	@Autowired
	SaleRepository saleRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Adds a new Store entity to the database.
	 * 
	 * @param store - the new Store to be added to the database
	 */
	public void addNewStore(Store store) {
		storeRepository.save(store);
	}
	
	/**
	 * Adds a new Sale entity to the database.
	 * 
	 * @param sale - the new Sale to be added to the database
	 */
	public void addNewSale(Sale sale) {
		saleRepository.save(sale);
	}
	
	/**
	 * Adds a new Product entity to the database.
	 * 
	 * @param product - the new Product to be added to the database
	 */
	public void addNewProduct(Product product) {
		productRepository.save(product);
	}
	
	/**
	 * Adds a new Order entity to the database.
	 * 
	 * @param order - the new Order to be added to the database
	 */
	public void addNewOrder(Order order) {
		orderRepository.save(order);
	}
	
	/**
	 * @return the List of all Order entities in the database
	 */
	public List <Order> getOrders(){
		return orderRepository.findAll();
	}
	
	/**
	 * @return the List of all Product entities in the database
	 */
	public List <Product> getProducts(){
		return productRepository.findAll();
	}
	
	/**
	 * Finds a product in the database and returns it using a given name and a Store
	 * It uses the Query By Example method for query creation
	 * 
	 * @param productName - name of the Product to find
	 * @param store - Store that houses the searched Product
	 * @return - the found Product if it exists, null otherwise
	 */
	public Product getProductByNameAndStore(String productName, Store store) {
		
		Product product = new Product();
		product.setStore(store);
		product.setName(productName);
		
		Example <Product> productExample = Example.of(product);
		List <Product> actual = productRepository.findAll(productExample);
		
		if(actual.size() > 0)
			return actual.get(0);
		
		else return null;
	}
	
	/**
	 * Updates the sum column for an existing Order
	 * 
	 * @param sum - the new sum for the Order
	 * @param order - the Order to be updated
	 */
	public void updateOrderSum(Float sum, Order order) {
		orderRepository.delete(order);
		order.setSum(sum);
		orderRepository.save(order);
	}
	
	/**
	 * Adds a new Product to an existing Order
	 * 
	 * @param product - the new Product to add
	 * @param order - the Order receiving the new Product
	 */
	public void addProductToOrder(Product product, Order order) {
		order.addProduct(product);
		orderRepository.flush();
	}
	
	/**
	 * Searches the database for a Store using a given name, and returns it 
	 * It uses the Query By Example method for query creation
	 * 
	 * @param storeName - the name of the Store to find
	 * @return - the found Store if it exists, null otherwise
	 */
	public Store findStoreByName(String storeName) {
		
		Store store = new Store();
		store.setName(storeName);
		
		Example <Store> storeExample = Example.of(store);
		
		List <Store> actual = storeRepository.findAll(storeExample);
		
		if(actual.size() > 0)
			return actual.get(0);
		else return null;
	}
	
	/**
	 * Updates the Stores in a given Order to have information about this new Order
	 * It uses the Query By Example method for query creation
	 * 
	 * @param order - the Order whose Stores are to be updated
	 */
	public void updateStores(Order order) {
		
		Iterator <Store> it = null;

		if(order.getStores().size() > 0)
			it = order.getStores().values().iterator();
		else return;
		
		while(it.hasNext()) {
			Store store = new Store();
			store.setName(it.next().getName());
			
			Example <Store> storeExample = Example.of(store);
			
			List <Store> actual = storeRepository.findAll(storeExample);
			
			if(actual.size() > 0)
				actual.get(0).addOrder(order);
		}
		
		storeRepository.flush();
	}
	
	/**
	 * Returns all the Products in a given Store
	 * It uses the Query By Example method for query creation
	 * 
	 * @param store - the Store whose Products are to be retrieved
	 * @return - the List of Products in the given Store
	 */
	public List <Product> getProducts(Store store){
		
		Product product = new Product();
		product.setStore(store);
		
		Example <Product> productExample = Example.of(product);
		
		return productRepository.findAll(productExample);
	}
	
	/**
	 * Returns all the Sales in a given Store
	 * It uses the Query By Example method for query creation
	 * 
	 * @param store - the Store whose Sales are to be retrieved
	 * @return - the List of Sales in the given Store
	 */
	public List <Sale> getProductsOnSale(Store store){
		
		Sale sale = new Sale();
		sale.setStore(store);
		
		Example <Sale> saleExample = Example.of(sale);
		
		return saleRepository.findAll(saleExample);
	}
	
	/**
	 * 
	 * Returns all the Orders in the database, ordered by the Order's sum in
	 * descending order, and then by the client's name in ascending order.
	 * It uses the Hibernate Criteria Queries method for query creation.
	 * 
	 * @return - the List of all ordered Orders
	 */
	public List <Order> getOrderedOrders(){

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> root = cq.from(Order.class);
		cq.orderBy(cb.desc(root.get("sum")), cb.asc(root.get("name")));
		
		return entityManager.createQuery(cq).getResultList();
	}
	
	/**
	 * Returns the first Product that matches the given name, price and Store.
	 * It uses the Query by Example technique for query creation.
	 * 
	 * @param productName - the name of the Product we are looking for
	 * @param price - the price of the Product we are looking for
	 * @param store - the Store we are searching in
	 * @return - the found Product if it exists, null otherwise
	 */
	public Product getProductByNameAndPrice(String productName, Float price, Store store) {
		
		Product product = new Product();
		product.setStore(store);
		product.setName(productName);
		
		Example <Product> productExample = Example.of(product);
		
		List <Product> products = productRepository.findAll(productExample);
	

		if(products.size() == 0)
			return null;
		
		Iterator <Product> it = products.iterator();
		
		while(it.hasNext()) {
			Product currentProduct = it.next();
			
			if(currentProduct.getPrice() - price == 0)
				return currentProduct;
		}
		
		return null;
	}

}
