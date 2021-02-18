package com.example.northWindFinal.api.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import com.example.northWindFinal.business.abstracts.IProductService;
import com.example.northWindFinal.entities.concretes.Product;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductsController {

  IProductService productService;

  @Autowired
  public void setProductService(IProductService productService) {
    this.productService = productService;
  }
  
  @GetMapping("/products/{id}")
  public ResponseEntity<Product> getById(@PathVariable(value = "id") Integer productId) {
    return Optional.ofNullable(productService.getById(productId))
        .map(category -> ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(category))
        .orElse(notFound().build());
  }

  @GetMapping("/products")
  public ResponseEntity<List<Product>> getAll() {
    return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE).body(productService.getAll());
  }

  @PostMapping("/products")
  public ResponseEntity<Map<String, Product>> add(@RequestBody Product product) {
    return created(fromCurrentRequest().build().toUri())
        .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(productService.add(product));
  }

  @PutMapping("/products/{id}")
  public ResponseEntity<Product> update(
      @PathVariable(value = "id") Integer productId, @RequestBody Product product)
      throws Exception {
    return ok().header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .body(productService.update(productId, product));
  }

  @DeleteMapping("products/{id}")
  public ResponseEntity<Map<String, Boolean>> delete(@PathVariable(value = "id") Integer productId)
      throws Exception {
    return ok().body(productService.delete(productId));
  }
}
