package co.morillas.core.domain;

public class Product {

    private Long id;
    private final String name;
    private final Long priceInCents;

    public Product(String name, Long priceInCents) {
        this.name = name;
        this.priceInCents = priceInCents;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPriceInCents() {
        return priceInCents;
    }
} 