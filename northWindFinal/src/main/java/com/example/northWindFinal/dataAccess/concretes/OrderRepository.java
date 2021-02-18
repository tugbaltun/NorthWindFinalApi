package com.example.northWindFinal.dataAccess.concretes;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.northWindFinal.entities.concretes.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
