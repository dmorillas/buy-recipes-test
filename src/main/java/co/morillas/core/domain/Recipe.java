package co.morillas.core.domain;

import java.util.List;

public class Recipe {

    private Long id;
    private final String name;
    private List<Product> products;

    public Recipe(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }
} 