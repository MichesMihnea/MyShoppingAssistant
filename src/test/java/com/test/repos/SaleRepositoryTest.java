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
import com.src.model.Sale;
import com.src.repos.SaleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class SaleRepositoryTest {
 
    @Autowired
    private SaleRepository saleRepository;
    
    @Test
    public void addSale() {
    	
    	int initialSize = saleRepository.findAll().size();

        Sale mySale = new Sale();
        mySale.setName("myTestSale");
        saleRepository.save(mySale);

        Sale sale = new Sale();
        sale.setName("myTestSale");
        
        int finalSize = saleRepository.findAll().size();
    
        assertThat(initialSize + 1).isEqualTo(finalSize);
    }
 
    @Test
    public void addSaleAndFindByName() {

        Sale mySale = new Sale();
        mySale.setName("myTestSale");
        saleRepository.save(mySale);

        Sale sale = new Sale();
        sale.setName("myTestSale");
        
        Example <Sale> exampleSale = Example.of(sale);
        Sale found = saleRepository.findAll(exampleSale).get(0);
    
        assertThat(found.getName()).isEqualTo(mySale.getName());
    }
    
    @Test
    public void addSaleAndDelete() {
    	
    	int initialSize = saleRepository.findAll().size();

        Sale mySale = new Sale();
        mySale.setName("myTestSale");
        saleRepository.save(mySale);

        Sale sale = new Sale();
        sale.setName("myTestSale");
        
        Example <Sale> exampleSale = Example.of(sale);
        Sale found = saleRepository.findAll(exampleSale).get(0);
        
        assertThat(saleRepository.findAll().size()).isEqualTo(initialSize + 1);
        
        saleRepository.delete(found);
           
        assertThat(saleRepository.findAll().size()).isEqualTo(initialSize);
    }

}