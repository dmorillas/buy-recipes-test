package co.morillas.core.service

import co.morillas.controller.CartResponse
import co.morillas.core.exception.NotFoundException
import co.morillas.repository.CartRepository
import co.morillas.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class CartService (
        private val cartRepository: CartRepository,
        private val recipeRepository: RecipeRepository,
){

    fun getCart(id: Long): CartResponse {
        val cart = cartRepository.findById(id).orElseThrow { NotFoundException("Cart with id $id not found") }
        return CartResponse.fromDomain(cart)
    }

    fun addRecipe(cartId: Long, recipeId: Long): CartResponse {
        val cart = cartRepository.findById(cartId).orElseThrow { NotFoundException("Cart with id $cartId not found") }
        val recipe = recipeRepository.findById(recipeId).orElseThrow { NotFoundException("Recipe with id $recipeId not found") }

        val cartProducts = cart.products
        recipe.products.filter { !cartProducts.contains(it) }.forEach { cart.addProduct(it) }
        val updatedCart = cartRepository.save(cart)

        return CartResponse.fromDomain(updatedCart)
    }

    fun removeRecipe(cartId: Long, recipeId: Long): CartResponse {
        val cart = cartRepository.findById(cartId).orElseThrow { NotFoundException("Cart with id $cartId not found") }
        val recipe = recipeRepository.findById(recipeId).orElseThrow { NotFoundException("Recipe with id $recipeId not found") }

        val cartProducts = cart.products
        recipe.products.filter { cartProducts.contains(it) }.forEach { cart.removeProduct(it) }
        val updatedCart = cartRepository.save(cart)

        return CartResponse.fromDomain(updatedCart)
    }
}
