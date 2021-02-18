package com.example.northWindFinal.dataAccess.concretes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.northWindFinal.entities.concretes.Product;

public interface ProductRepository extends JpaRepository<Product,Integer>{

	
}
