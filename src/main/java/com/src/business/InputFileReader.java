package com.src.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.src.model.Order;
import com.src.model.Product;
import com.src.model.Sale;
import com.src.model.Store;

/**
 * @author Mihnea
 * This class handles the processing of the input Files.
 * There are two types of input files: Store information Files, and Order information Files.
 * We suppose that all the Files are in the same folder.
 * 
 */
public class InputFileReader {

	DBUtils utils;//Access to the database, passed down through the constructor
	
	File resourceFolder;//The folder in which we are looking for the input Files
	//Again, suppose they are all in the same folder.
	File[] storeFiles;//The array of  Store information Files
	File[] orderFiles;//The array of Order information Files
	
	
	/**
	 * The public constructor of this class.
	 * After receiving the folder's name, it proceeds to populate its two File arrays with
	 * the Files it finds in the folder that satisfy the naming conventions for our two
	 * types of Files.
	 * 
	 * @param folderPath - the path to the folder containing the Files we are going to process
	 * @param utils - the database access, coming down from the application's driver class
	 * @throws FileNotFoundException - if the folder does not exist
	 */
	public InputFileReader(String folderPath, DBUtils utils) throws FileNotFoundException {
		resourceFolder = new File(folderPath);
		this.utils = utils;
		
		storeFiles = resourceFolder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.startsWith("magazin_");
		    }
		});
		
		orderFiles = resourceFolder.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.startsWith("comanda_");
		    }
		});
	}
	
	/**
	 * The "main" method of the class, it starts the process of reading the input Files.
	 * The File names do hold significant information. For the Store information Files, they provide
	 * the name of the Store. 
	 * For the Order information Files, they provide the name of the customer, and also the Date of the
	 * Order, which is to be parsed.
	 * 
	 * @throws IOException - something went wrong creating the FileReader, or propagated from the called methods
	 * @throws ParseException - the Date in the name of the Order information File can not be parsed
	 */
	public void processInput() throws IOException, ParseException {
		
		for(File currentFile : storeFiles) {
			//Open the File for reading
			BufferedReader reader = new BufferedReader(new FileReader(currentFile.getAbsolutePath()));
			//Extract the name of the Store from the File's name
			String storeName = currentFile.getName().split("_")[1];
			this.processStoreFile(reader, storeName.substring(0, storeName.length() - 4));
		}
		
		for(File currentFile : orderFiles) {
			//Open the File for reading
			BufferedReader reader = new BufferedReader(new FileReader(currentFile.getAbsolutePath()));
			//Extract the name of the customer from the File's name
			String customerName = currentFile.getName().split("_")[1];
			//Extract the Date of the Order from the File's name
			Date orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(currentFile.getName().split("_")[2]
					.substring(0, currentFile.getName().split("_")[2].length() - 4));
			this.processOrderFile(reader, customerName, orderDate);
		}
	}
	
	/**
	 * Method handling the processing of the Order information Files.
	 * In case reading exceptions are thrown, the stack trace is printed and the
	 * processing moves on.
	 * 
	 * @param reader - the already opened BufferedReader for the File of interest
	 * @param customerName - the name of the customer, which we determined when the File was opened
	 * @param orderDate - the Date of the Order, which we determined when the File was opened
	 * @throws IOException - in case the BufferedReader can not be closed
	 */
	public void processOrderFile(BufferedReader reader, String customerName, Date orderDate) throws IOException {
		
		//One Order information File yields one Order entity. Create it.
		//Uses finally blocks to be 100% sure the application does not remain stuck in while loops.
		
		Order order = new Order();
		order.setName(customerName);
		order.setDate(orderDate);
		
		String requirements = null;
		
		//Attempt to read the Order's requirements
		//It's a String of names separated by semicolons
		
		try {
			requirements = reader.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		order.setRequirements(requirements);
		String storeInfo = null;
		
		try {
			storeInfo = reader.readLine();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		//The Stores and their priority. This was a pretty serious issue since I was trying to store
		//the Stores in a HashSet, ordered by their priority. It did work, until they had to be persisted.
		//When persisting this HashSet, and then retrieving it, the Stores would no longer be in order.
		//They would always come out of the database in a 100% random order.
		//The solution was persisting them in a Map, that maps a priority to a Store and this way,
		//they are always ordered in the database.
		
		Map <Integer, Store> storesByPriority = new HashMap <Integer, Store> ();
		
		//Read and process Store information according to the given File format.
		
		while(storeInfo != null) {
			
			try {
				String[] storeAndPriority = storeInfo.split(":");
				Store currentStore = utils.findStoreByName(storeAndPriority[0]);
				storesByPriority.put(Integer.parseInt(storeAndPriority[1]), currentStore);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					storeInfo = reader.readLine();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//Update the Order and the Stores according to their Many to Many relationship
		//Then persist the new Order.
		
		order.setStores(storesByPriority);
		
		utils.updateStores(order);
		utils.addNewOrder(order);
		
		reader.close();
	}
	
	public void processStoreFile(BufferedReader reader, String storeName) throws IOException, ParseException {
		
		//The Store information File holds information regarding a single Store entity. Create it.
		//Uses finally blocks to be 100% sure the application does not remain stuck in while loops.
		
		Store store = new Store();
		
		//Retrieve all the information, according to the given File format.
		
		try {
			String openOnSundays = reader.readLine();
			
			if(openOnSundays.equals("Deschis")) {
				store.setOpenOnSunday(true);
				
			}else store.setOpenOnSunday(false);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		store.setName(storeName);
		
		//Persist the new Store
		
		utils.addNewStore(store);
		
		String lineOfDates = null;
		
		try {
			reader.readLine();
			
			lineOfDates = reader.readLine();
		}
		catch(Exception e) {
			
		}
		
		boolean doneWithSales = false;
		
		//Read the Sales information
		//Problems could appear with parsing the Dates and prices
		
		while(!doneWithSales) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String[] dates = null;
			String saleInfo = "";
			
			try {
				dates = lineOfDates.split(" ");
				saleInfo = reader.readLine();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			while(!saleInfo.isBlank() && !saleInfo.contains("*")) {
				
				try {
					String[] productAndPrice = saleInfo.split(":");
					
					//Create a new Sale
					//Try to set values accordingly
					
					Sale sale = new Sale();
					sale.setStartDate(sdf.parse(dates[0]));
					
					if(dates.length > 1) {
						sale.setEndDate(sdf.parse(dates[1]));
					}
					else sale.setEndDate(sdf.parse(dates[0]));
					
					sale.setName(productAndPrice[0]);
					sale.setPrice(Float.parseFloat(productAndPrice[1]));
					sale.setStore(store);
					store.addSale(sale);
					
					//Persist the new Sale
					
					utils.addNewSale(sale);
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
				saleInfo = reader.readLine();
				
				if(saleInfo.contains("*"))
					doneWithSales = true;
				}		
			}
			
			if(!doneWithSales)
				lineOfDates = reader.readLine();
		}
		
		String productInfo = reader.readLine();
		
		//Now read the Products that are not on Sale
		
		while(productInfo != null) {
			
			try {
			String[] nameAndPrice = productInfo.split(":");
			
			//Create a new Product
			//Try to set values accordingly
			
			Product product = new Product();
			
			product.setName(nameAndPrice[0]);

			product.setPrice(Float.parseFloat(nameAndPrice[1]));
			product.setStore(store);
			
			store.addProduct(product);
			
			//Persist the new Product
			
			utils.addNewProduct(product);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			productInfo = reader.readLine();
			}
			
		}
		
		reader.close();
		
	}
}
