package com.test.repos;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import com.src.application.MyShoppingAssistantApplication;
import com.src.model.Order;
import com.src.repos.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class OrderRepositoryTest {
 
    @Autowired
    private OrderRepository orderRepository;
    
    @Test
    public void addOrder() {
    	
    	int initialSize = orderRepository.findAll().size();

        Order myOrder = new Order();
        myOrder.setName("myTestOrder");
        orderRepository.save(myOrder);

        Order order = new Order();
        order.setName("myTestOrder");
        
        int finalSize = orderRepository.findAll().size();
    
        assertThat(initialSize + 1).isEqualTo(finalSize);
    }
 
    @Test
    public void addOrderAndFindByName() {

        Order myOrder = new Order();
        myOrder.setName("myTestOrder");
        orderRepository.save(myOrder);

        Order order = new Order();
        order.setName("myTestOrder");
        
        Example <Order> exampleOrder = Example.of(order);
        Order found = orderRepository.findAll(exampleOrder).get(0);
    
        assertThat(found.getName()).isEqualTo(myOrder.getName());
    }
    
    @Test
    public void addOrderAndDelete() {
    	
    	int initialSize = orderRepository.findAll().size();

        Order myOrder = new Order();
        myOrder.setName("myTestOrder");
        orderRepository.save(myOrder);

        Order order = new Order();
        order.setName("myTestOrder");
        
        Example <Order> exampleOrder = Example.of(order);
        Order found = orderRepository.findAll(exampleOrder).get(0);
        
        assertThat(orderRepository.findAll().size()).isEqualTo(initialSize + 1);
        
        orderRepository.delete(found);
           
        assertThat(orderRepository.findAll().size()).isEqualTo(initialSize);
    }

}