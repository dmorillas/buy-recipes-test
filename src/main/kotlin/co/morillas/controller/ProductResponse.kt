package co.morillas.controller

import co.morillas.core.domain.Product

data class ProductResponse (
    val id: Long?,
    val name: String,
    val priceInCents: Int,
) {
    companion object {
        fun fromDomain(product: Product): ProductResponse {
            return ProductResponse (product.id, product.name, product.priceInCents)
        }
    }
}
