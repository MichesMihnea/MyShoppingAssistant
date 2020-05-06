package com.test.repos;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import com.src.application.MyShoppingAssistantApplication;
import com.src.model.Product;
import com.src.repos.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class ProductRepositoryTest {
 
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    public void addProduct() {
    	
    	int initialSize = productRepository.findAll().size();

        Product myProduct = new Product();
        myProduct.setName("myTestProduct");
        productRepository.save(myProduct);

        Product product = new Product();
        product.setName("myTestProduct");
        
        int finalSize = productRepository.findAll().size();
    
        assertThat(initialSize + 1).isEqualTo(finalSize);
    }
 
    @Test
    public void addProductAndFindByName() {

        Product myProduct = new Product();
        myProduct.setName("myTestProduct");
        productRepository.save(myProduct);

        Product product = new Product();
        product.setName("myTestProduct");
        
        Example <Product> exampleProduct = Example.of(product);
        Product found = productRepository.findAll(exampleProduct).get(0);
    
        assertThat(found.getName()).isEqualTo(myProduct.getName());
    }
    
    @Test
    public void addProductAndDelete() {
    	
    	int initialSize = productRepository.findAll().size();

        Product myProduct = new Product();
        myProduct.setName("myTestProduct");
        productRepository.save(myProduct);

        Product product = new Product();
        product.setName("myTestProduct");
        
        Example <Product> exampleProduct = Example.of(product);
        Product found = productRepository.findAll(exampleProduct).get(0);
        
        assertThat(productRepository.findAll().size()).isEqualTo(initialSize + 1);
        
        productRepository.delete(found);
           
        assertThat(productRepository.findAll().size()).isEqualTo(initialSize);
    }

}