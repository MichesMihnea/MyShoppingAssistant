package com.src.application;

import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.src.business.DBUtils;
import com.src.business.InputFileReader;
import com.src.business.OutputFileWriter;
import com.src.business.ShoppingAssistant;
import com.src.model.Order;
import com.src.model.Product;
import com.src.model.Sale;
import com.src.model.Store;

/**
 * @author Mihnea
 * This is the driver class for the application.
 */
@SpringBootApplication(scanBasePackages = "com")//This tells Spring that this is the driver class of the application
@EntityScan("com.src.model")//This tells Spring where to look for entities, for the dependency injection
@EnableJpaRepositories("com.src.repos")//This tells Spring where the repositories are located

public class MyShoppingAssistantApplication implements CommandLineRunner {
	//CommandLineRunner, for short, means that when this application is started, this class calls its run(args) method.
	
	@Autowired//This tells Spring to inject a dependency here
	DBUtils utils;//Database access class

	public static void main(String[] args) {
		SpringApplication.run(MyShoppingAssistantApplication.class, args);
	}
	
	//This is where the magic happens
	public void run(String... args) throws Exception 
    {  
		//Read the input files. Remember that the InputFileReader's constructor takes one argument: the path
		//to the folder containing the input Files, and another argument which represents the DBUtils instance
		InputFileReader ifr = new InputFileReader("C:\\Users\\Mihnea\\eclipse-workspace\\MyShoppingAssistant\\resources", utils);
		//Process the input and persist it
		ifr.processInput();
		//The main logic class
		ShoppingAssistant sa = new ShoppingAssistant(utils);
		//The output generator, whose argument is the name of the output text file
		OutputFileWriter ofw = new OutputFileWriter("comenzi.txt");
		//This basically means "solve the problem" : it gets the ShoppingAssistant to generate a List
		//of Orders, which are fulfilled, and then the OutputFileWriter will print them to a File.
		ofw.writeOutputFile(sa.generateOutput());
    }

}
