package co.morillas.controller;

import co.morillas.core.domain.Recipe;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeResponse {
    private final Long id;
    private final String name;
    private final List<ProductResponse> products;

    public RecipeResponse(Long id, String name, List<ProductResponse> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public static RecipeResponse fromDomain(Recipe recipe) {
        List<ProductResponse> products = recipe.getProducts().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());

        return new RecipeResponse(recipe.getId(), recipe.getName(), products);
    }
}
