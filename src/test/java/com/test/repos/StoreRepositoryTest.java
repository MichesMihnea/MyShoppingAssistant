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
import com.src.model.Store;
import com.src.repos.StoreRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyShoppingAssistantApplication.class)
public class StoreRepositoryTest {
 
    @Autowired
    private StoreRepository storeRepository;
    
    @Test
    public void addStore() {
    	
    	int initialSize = storeRepository.findAll().size();

        Store myStore = new Store();
        myStore.setName("myTestStore");
        storeRepository.save(myStore);

        Store store = new Store();
        store.setName("myTestStore");
        
        int finalSize = storeRepository.findAll().size();
    
        assertThat(initialSize + 1).isEqualTo(finalSize);
    }
 
    @Test
    public void addStoreAndFindByName() {

        Store myStore = new Store();
        myStore.setName("myTestStore");
        storeRepository.save(myStore);

        Store store = new Store();
        store.setName("myTestStore");
        
        Example <Store> exampleStore = Example.of(store);
        Store found = storeRepository.findAll(exampleStore).get(0);
    
        assertThat(found.getName()).isEqualTo(myStore.getName());
    }
    
    @Test
    public void addStoreAndDelete() {
    	
    	int initialSize = storeRepository.findAll().size();

        Store myStore = new Store();
        myStore.setName("myTestStore");
        storeRepository.save(myStore);

        Store store = new Store();
        store.setName("myTestStore");
        
        Example <Store> exampleStore = Example.of(store);
        Store found = storeRepository.findAll(exampleStore).get(0);
        
        assertThat(storeRepository.findAll().size()).isEqualTo(initialSize + 1);
        
        storeRepository.delete(found);
           
        assertThat(storeRepository.findAll().size()).isEqualTo(initialSize);
    }

}