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
import com.src.model.Order;
import com.src.repos.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class OrderTest {

	@Test
	public void createOrder() {
		Order order = new Order();
		
		assertThat(order).isNotNull();
	}
	
	@Test
	public void setName() {
		Order order = new Order();
		order.setName("myOrder");
		
		assertThat(order.getName()).isEqualTo("myOrder");
	}
	
	@Test
	public void setSum() {
		Order order = new Order();
		order.setSum(5.15f);
		
		assertThat(order.getSum()).isEqualTo(5.15f);
	}
	
	@Test
	public void setDate() {
		Date currentDate = new Date(System.currentTimeMillis());
		Order order = new Order();
		order.setDate(currentDate);
		
		assertThat(order.getDate()).isEqualTo(currentDate);
	}
	
	@Test
	public void setRequirements() {
		Order order = new Order();
		order.setRequirements("myRequirements");
		
		assertThat(order.getRequirements()).isEqualTo("myRequirements");
	}
	
}
