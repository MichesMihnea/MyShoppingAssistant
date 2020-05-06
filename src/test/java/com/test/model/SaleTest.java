package com.test.model;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import com.src.application.MyShoppingAssistantApplication;
import com.src.model.Order;
import com.src.model.Product;
import com.src.model.Sale;
import com.src.repos.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class SaleTest {

	@Test
	public void createSale() {
		Sale sale = new Sale();
		
		assertThat(sale).isNotNull();
	}
	
	@Test
	public void setName() {
		Sale sale = new Sale();
		sale.setName("mySale");
		
		assertThat(sale.getName()).isEqualTo("mySale");
	}
	
	@Test
	public void setPrice() {
		Sale sale = new Sale();
		sale.setPrice(3.13f);
		
		assertThat(sale.getPrice()).isEqualTo(3.13f);
	}
	
	@Test
	public void setStartDate() {
		Date currentDate = new Date(System.currentTimeMillis());
		Sale sale = new Sale();
		sale.setStartDate(currentDate);
		
		assertThat(sale.getStartDate()).isEqualTo(currentDate);
	}
	
	@Test
	public void setEndDate() {
		Date currentDate = new Date(System.currentTimeMillis());
		Sale sale = new Sale();
		sale.setEndDate(currentDate);
		
		assertThat(sale.getEndDate()).isEqualTo(currentDate);
	}
	
}
