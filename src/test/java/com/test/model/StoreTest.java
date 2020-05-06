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
import com.src.model.Store;
import com.src.repos.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class StoreTest {

	@Test
	public void createStore() {
		Store store = new Store();
		
		assertThat(store).isNotNull();
	}
	
	@Test
	public void setName() {
		Store store = new Store();
		store.setName("myStore");
		
		assertThat(store.getName()).isEqualTo("myStore");
	}
	
	@Test
	public void setOpenOnSunday() {
		Store store = new Store();
		store.setOpenOnSunday(true);
		
		assertThat(store.getOpenOnSunday()).isEqualTo(true);
	}
	
}
