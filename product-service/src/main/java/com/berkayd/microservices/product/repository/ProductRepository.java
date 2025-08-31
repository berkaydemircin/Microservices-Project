package com.berkayd.microservices.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.berkayd.microservices.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
