package co.morillas.controller

import co.morillas.core.domain.Cart

class CartResponse(
    val id: Long?,
    val totalInCents: Int,
    val products: List<ProductResponse>
) {
    companion object {
        fun fromDomain(cart: Cart): CartResponse {
            val products = cart.products.map { ProductResponse.fromDomain(it) }
            return CartResponse(cart.id, cart.totalInCents, products)
        }
    }
}
