package com.src.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.src.model.Product;

/**
 * @author Mihnea
 * The data repository for the Product entity. A simple description of this interface is basically a bridge
 * between the application and the database.
 * Due to the simplicity of the application, no methods
 * had to be implemented here, as simple queries by Id were enough.
 */

@Repository//This tells Spring that this Interface is a repository, and so it can
//find it when looking for dependencies.
public interface ProductRepository extends JpaRepository <Product, Long>{

}
