package com.src.business;

import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.src.model.Order;
import com.src.model.Product;
import com.src.model.Sale;
import com.src.model.Store;

/**
 * @author Mihnea
 * This class handles the main logic of the application.
 * It has access to the database, passed down from the application's driver class.
 *
 */
public class ShoppingAssistant {

	DBUtils utils;
	
	/**
	 * Public constructor of the application, with no extra logic.
	 * 
	 * @param utils - access to the database
	 */
	public ShoppingAssistant(DBUtils utils) {
		this.utils = utils;
	}
	
	/**
	 * Takes an order, analyzes its requirements and then searches for the requirements
	 * in the Stores, in the given customer's priority. It then updates the Order's Product
	 * and Store Lists.
	 * 
	 * @param order - the Order to be fulfilled
	 */
	public void fulfillOrder(Order order) {
		
		//The requirements, which are a String of names separated by semicolons
		String[] requirements = order.getRequirements().split(";");
		//The Stores, ordered in descending order of priority
		Set <Store> stores = (Set<Store>) order.getStores().values();
		Float sum = 0f;
		
		for(String requirement : requirements) {
			//For each requirement of this Order
			//Find the first price of this requirement, i.e. the first occurence of the
			//requirement in a store in which it is not on sale(we will need this in case we don't find
			//this requirement in a Sale at all
			Float firstPrice = 0f;
			Float bestPrice = 0f;
			boolean foundRequirementOnSale = false;
			boolean requirementExists = false;
			Store foundStore = null;
			Product firstPriceProduct = null;
						
			//Search the Stores for this requirement (remember, the Stores are
			//already ordered by descending priority)
			for(Store store : stores) {
				
				Date now = new Date(System.currentTimeMillis());
		        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
		        
		        //Is the Store open today?
		        
		        if(simpleDateFormat.format(now).equals("Sun") && store.getOpenOnSunday() == false) {
		        	continue;
		        }

				//Is the requirement on Sale here?
				if(checkSale(requirement, store)) {
					//We found it, time to stop searching
					sum += getLowestPrice(requirement, store);
					bestPrice += getLowestPrice(requirement, store);
					foundStore = store;
					foundRequirementOnSale = true;
					requirementExists = true;
					break;
				}
				else if(firstPrice == 0f) {
					//It's not on sale. Can we find it for full price?
					//If so, store this information for future use. But only for the first occurence.

					firstPriceProduct = getFirstPrice(requirement, store);
					if(firstPriceProduct != null) {

						requirementExists = true;
						firstPrice = firstPriceProduct.getPrice();
					}
				}
			}
			
			if(!foundRequirementOnSale && firstPriceProduct != null) {
				//The requirement was not on Sale in any Store, but we found it for full price
				sum += firstPrice;
				foundStore = firstPriceProduct.getStore();
				requirementExists = true;
			}
			
			if(requirementExists) {
				//The requirement was found in one way or another, add it to the Order
				Product product;			
				if(bestPrice != 0f)
					product = utils.getProductByNameAndPrice(requirement, bestPrice, foundStore);
				else product = utils.getProductByNameAndPrice(requirement, firstPrice, foundStore);
				
				product.addOrder(order);
			
				utils.addProductToOrder(product, order);
			}else {
				//The requirement was not found. We will signal this by adding a new Product to the Order,
				//but setting its price to 0f.
				Product product = new Product();
				product.setName(requirement);
				product.setPrice(0f);
				utils.addNewProduct(product);
				utils.addProductToOrder(product, order);
			}
			
		}
		
		//After shopping, we have to persist the total sum of the Order
		utils.updateOrderSum(sum, order);
	}
	
	/**
	 * This method checks if a given requirement is on Sale in a given Store.
	 * 
	 * @param requirement - requirement to search for
	 * @param store - Store to search
	 * @return - true if the requirement is on Sale in this Store, false otherwise
	 */
	public boolean checkSale(String requirement, Store store) {
		
		//Get all the Sales in this Store
		List <Sale> sales = utils.getProductsOnSale(store);
		Iterator <Sale> it = sales.iterator();
		
		//Iterate through them
		//Could have also used Query by Example here, but I wanted to implement multiple methods
		//for querying.
		while(it.hasNext()) {
			Sale currentSale = it.next();
			
			//Found the requirement on Sale?
			if(currentSale.getName().equals(requirement)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the lowest price of a requirement in a given Store.
	 * 
	 * @param requirement - requirement whose lowest price is to be found
	 * @param store - Store in which to search
	 * @return - the lowest price of the requirement if found, 0f otherwise
	 */
	public Float getLowestPrice(String requirement, Store store) {
		
		//Get all the Sales in this Store
		List <Sale> sales = utils.getProductsOnSale(store);
		Iterator <Sale> it = sales.iterator();
		Long minDiffInMillies = 0l;
		Float lowestPrice = 0f;
		
		//Iterate through them
		//Could have also used Query by Example here, but I wanted to implement multiple methods
		//for querying.
		
		while(it.hasNext()) {
			Sale currentSale = it.next();
			
			//Found the requirement on sale?
			if(currentSale.getName().equals(requirement)) {
				
				//The shortest Sale period determines the lowest price!
				//How can we compare Sale intervals?
				//We consider the interval as the difference of the two end Dates of the interval, in milliseconds
				//And then we find the minimum of this difference
				
				Long diffInMillies = currentSale.getEndDate().getTime() - currentSale.getStartDate().getTime();
				
				if(minDiffInMillies == 0l) {
					minDiffInMillies = diffInMillies;
					lowestPrice = currentSale.getPrice();
				}else if(minDiffInMillies > diffInMillies) {
					minDiffInMillies = diffInMillies;
					lowestPrice = currentSale.getPrice();
				}
			}
		}
		
		return lowestPrice;
	}
	
	/**
	 * This method returns the price of the first occurence of a requirement in a Store.
	 * It ignores Sales.
	 * 
	 * @param requirement - requirement to search for
	 * @param store - Store to be searched
	 * @return - the Product entity representing the found Requirement
	 */
	public Product getFirstPrice(String requirement, Store store) {
		
		//Get all the Products in the Store
		
		List <Product> products = utils.getProducts(store);
		Iterator <Product> it = products.iterator();
		
		//Iterate through them
		//Could have also used Query by Example here, but I wanted to implement multiple methods
		//for querying.
		
		while(it.hasNext()) {
			Product currentProduct = it.next();
			
			if(currentProduct.getName().equals(requirement)) {
				return currentProduct;
			}
		}
		
		return null;
	}
	
	/**
	 * The "main" method of this class. It retrieves the Orders from the database, then
	 * iterates through them and fulfills them, and returns the List of fulfilled Orders.
	 * 
	 * @return - the List of fulfilled Orders
	 */
	public List <Order> generateOutput() {
		for(Order order : utils.getOrders()) {
			this.fulfillOrder(order);
		}
		
		return utils.getOrderedOrders();
	}
}
