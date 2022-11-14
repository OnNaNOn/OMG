package com.ono.omg.controller;

import com.ono.omg.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTempRepository extends JpaRepository<Product, Long> {

}
