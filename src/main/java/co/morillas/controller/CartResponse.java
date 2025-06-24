package co.morillas.controller;

import co.morillas.core.domain.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {
    private final Long id;
    private final Integer totalInCents;
    private final List<ProductResponse> products;

    public CartResponse(Long id, Integer totalInCents, List<ProductResponse> products) {
        this.id = id;
        this.totalInCents = totalInCents;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public Integer getTotalInCents() {
        return totalInCents;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public static CartResponse fromDomain(Cart cart) {
        List<ProductResponse> products = cart.getProducts().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());

        return new CartResponse(cart.getId(), cart.getTotalInCents(), products);
    }
}
