package com.ProductManagement.ProductManagement.service.impl;

import com.ProductManagement.ProductManagement.model.Product;
import com.ProductManagement.ProductManagement.repository.ProductRepository;
import com.ProductManagement.ProductManagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProducts(String name) {
        return productRepository.findByProductNameIgnoreCase(name);
    }

    @Override
    public void deleteProductByName(String name) {
        List<Product> products = productRepository.findByProductNameIgnoreCase(name);
        for (Product p : products) {
            productRepository.delete(p);
        }
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setProductName(updatedProduct.getProductName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

}
