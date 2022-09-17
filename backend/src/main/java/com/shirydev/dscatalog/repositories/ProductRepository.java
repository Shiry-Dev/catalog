package com.shirydev.dscatalog.repositories;

import com.shirydev.dscatalog.entities.Category;
import com.shirydev.dscatalog.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


}
