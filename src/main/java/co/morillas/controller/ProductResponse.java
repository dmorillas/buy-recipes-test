package co.morillas.controller;

import co.morillas.core.domain.Product;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final Integer priceInCents;

    public ProductResponse(Long id, String name, Integer priceInCents) {
        this.id = id;
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPriceInCents() {
        return priceInCents;
    }

    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPriceInCents());
    }
}
