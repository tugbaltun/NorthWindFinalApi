package com.example.northWindFinal.api.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.northWindFinal.business.abstracts.ISellingService;
import com.example.northWindFinal.entities.concretes.Order;
import com.example.northWindFinal.entities.concretes.OrderDetail;

@RestController
@RequestMapping("api/v1")
public class SellingController {
	
	ISellingService sellingService;
	
	@Autowired
	public void setSellingService(ISellingService sellingService) {
		this.sellingService = sellingService;
	}
	
	@GetMapping("/selling/order/{id}")
	public ResponseEntity<Order> getById(@PathVariable(value = "id") Integer id) {
	  return Optional.ofNullable(sellingService.getOrderById(id))
	      .map(order -> ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(order))
	      .orElse(notFound().build());
	}
	
	@PostMapping("/selling/order")
	public ResponseEntity<Order> addOrder(@RequestBody Order order) {
		return created(fromCurrentRequest().build().toUri()).header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
				.body(sellingService.addOrder(order));
	}

	@PutMapping("/selling/order/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable(value = "id") Integer orderId, @RequestBody Order order) {
		return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(sellingService.updateOrder(orderId, order));
	}
	
	@GetMapping("/selling/orderDetail/{id}")
	public ResponseEntity<List<OrderDetail>> getCardByOrderId(@PathVariable(value="id") Integer orderId){
		return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(sellingService.getByOrderId(orderId));
	}
	
	@PostMapping("/selling/orderDetail/{id}")
	public ResponseEntity<Map<String, List<OrderDetail>>> addOrderDetail(@PathVariable(name="id") String customerId){
		return created(fromCurrentRequest().build().toUri())
		        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
		        .body(sellingService.addCardToOrderDetail(customerId));
	}
	

	  
	  
}
