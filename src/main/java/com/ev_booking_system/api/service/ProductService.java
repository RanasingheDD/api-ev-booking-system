package com.ProductManagement.ProductManagement.service;

import com.ProductManagement.ProductManagement.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product addProduct(Product product);
    List<Product> getAllProducts();
    List<Product> searchProducts(String name);
    void deleteProductByName(String name);
    Optional<Product> getProductById(Long id);
    Product updateProduct(Long id, Product updatedProduct);

    void deleteProductById(Long id);
}
