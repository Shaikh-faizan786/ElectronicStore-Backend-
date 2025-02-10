package com.electronics.store.repositories;

import com.electronics.store.dtos.PageableResponse;
import com.electronics.store.entities.Category;
import com.electronics.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product>  findByTitleContaining(String subTitle,Pageable pageable);

    Page<Product> findByLiveTrue(Pageable pageable);
    Page<Product> findByCategory(Category category,Pageable pageable);

}
