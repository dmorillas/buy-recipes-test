package co.morillas.core.domain;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private Long id;
    private Long totalInCents;
    private List<Product> products = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }
} 