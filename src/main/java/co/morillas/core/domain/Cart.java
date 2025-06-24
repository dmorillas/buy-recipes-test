package co.morillas.core.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer totalInCents = 0;

    @ManyToMany
    @JoinTable(
        name = "cart_items",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    public Cart() {}

    public Cart(Long id, Integer totalInCents, List<Product> products) {
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

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        totalInCents += product.getPriceInCents();
    }

    public void removeProduct(Product product) {
        products.remove(product);
        totalInCents -= product.getPriceInCents();
    }
} 