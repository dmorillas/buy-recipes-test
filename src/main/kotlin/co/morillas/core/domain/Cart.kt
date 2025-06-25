package co.morillas.core.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "cart")
class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var totalInCents: Int = 0

    @ManyToMany
    @JoinTable(
        name = "cart_items",
        joinColumns = [JoinColumn(name = "cart_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    var products: MutableList<Product> = mutableListOf()

    constructor()

    constructor(id: Long, totalInCents: Int, products: MutableList<Product>) {
        this.id = id
        this.totalInCents = totalInCents
        this.products = products
    }

    fun addProduct(product: Product) {
        products.add(product)
        totalInCents += product.priceInCents
    }

    fun removeProduct(product: Product) {
        products.remove(product)
        totalInCents -= product.priceInCents
    }
}