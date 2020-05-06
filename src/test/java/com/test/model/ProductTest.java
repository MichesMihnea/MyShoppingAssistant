package com.test.model;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import com.src.application.MyShoppingAssistantApplication;
import com.src.model.Order;
import com.src.model.Product;
import com.src.repos.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class ProductTest {

	@Test
	public void createProduct() {
		Product product = new Product();
		
		assertThat(product).isNotNull();
	}
	
	@Test
	public void setName() {
		Product product = new Product();
		product.setName("myProduct");
		
		assertThat(product.getName()).isEqualTo("myProduct");
	}
	
	@Test
	public void setPrice() {
		Product product = new Product();
		product.setPrice(3.13f);
		
		assertThat(product.getPrice()).isEqualTo(3.13f);
	}
	
}
