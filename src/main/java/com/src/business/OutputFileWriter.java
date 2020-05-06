package com.src.business;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.src.model.Order;
import com.src.model.Product;

/**
 * @author Mihnea
 * This class handles the generation of the output text File.
 * It basically takes a List of Orders and prints it nicely.
 * Thanks to the relationships the Order entity has established, this feature
 * is extremely easy to implement, as all the information we need is kept can be
 * retrieved using the Order entity.
 * The generated output File can be found in the project's main folder.
 * 
 */
public class OutputFileWriter {
	
	private String fileName;
	
	/**
	 * The public constructor of the class. It simply receives a file name.
	 * 
	 * @param fileName - the name of the output File
	 */
	public OutputFileWriter(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * One single method to handle the text file's creation, since only one iteration
	 * through a List is required.
	 * 
	 * @param orders - the list of Orders to be printed to the file
	 * @throws FileNotFoundException - in case the creation of the PrintWriter fails
	 * @throws UnsupportedEncodingException - in case the creation of the PrintWriter fails
	 */
	public void writeOutputFile(List <Order> orders) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		
		Iterator <Order> it = orders.iterator();
		
		//Iterate through the List of Orders
		
		while(it.hasNext()) {
			
			//Round the price to two decimal places
			
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			
			Order currentOrder = it.next();
			writer.println("Customer Name: " + currentOrder.getName());
			writer.println("Total sum: " + df.format(currentOrder.getSum()));
			
			//The Order has a List of Products that appear in the Order. Iterate through it.
			
			Set <Product> products = currentOrder.getProducts();
			Iterator <Product> pit = products.iterator();
			
			while(pit.hasNext()) {
				Product product = pit.next();
				
				//If a Product was not found, its price is set to 0f. This conditional block handles that,
				
				if(product.getPrice() != 0) {
					writer.println("Product: " + product.getName() + " Price: " + product.getPrice() + " Store: " + product.getStore().getName());
				}else writer.println("Product: " + product.getName() + " can not be found in the stores.");
			}
			
			writer.println();
			writer.println();
			writer.println();
		}
		writer.close();
	}
}
