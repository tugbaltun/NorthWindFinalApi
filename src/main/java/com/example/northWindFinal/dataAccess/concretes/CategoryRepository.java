package com.example.northWindFinal.dataAccess.concretes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.northWindFinal.entities.concretes.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
