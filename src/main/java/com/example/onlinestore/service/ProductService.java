package com.example.onlinestore.service;

import com.example.onlinestore.model.Product;
import com.example.onlinestore.repository.ProductRepository;
import com.example.onlinestore.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product product) {
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (product.getQuantityInStock() < 0) {
            throw new IllegalArgumentException("Quantity in stock cannot be negative.");
        }
        return repo.save(product);
    }

    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product %d not found".formatted(id)));
    }

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product update(Long id, Product updated) {
        return repo.findById(id)
                .map(existing -> {
                    if (updated.getName() != null && !updated.getName().isBlank()) {
                        existing.setName(updated.getName());
                    }
                    if (updated.getDescription() != null) {
                        existing.setDescription(updated.getDescription());
                    }
                    if (updated.getPrice() >= 0) {
                        existing.setPrice(updated.getPrice());
                    } else if (updated.getPrice() < 0) {
                        throw new IllegalArgumentException("Price cannot be negative.");
                    }
                    if (updated.getQuantityInStock() >= 0) {
                        existing.setQuantityInStock(updated.getQuantityInStock());
                    } else if (updated.getQuantityInStock() < 0) {
                        throw new IllegalArgumentException("Quantity in stock cannot be negative.");
                    }
                    return repo.save(existing);
                })
                .orElseThrow(() -> new NotFoundException("Product %d not found".formatted(id)));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Product %d not found".formatted(id));
        }
        repo.deleteById(id);
    }
}
