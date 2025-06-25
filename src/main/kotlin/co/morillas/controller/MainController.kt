package co.morillas.controller

import co.morillas.core.service.CartService
import co.morillas.core.service.RecipeService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
class MainController(
    private val cartService: CartService,
    private val recipeService: RecipeService,
) {


    @GetMapping("/recipes")
    fun getAllRecipes(): List<RecipeResponse> {
        return recipeService.getRecipes()
    }

    @GetMapping("/carts/{id}")
    fun getCart(@PathVariable id: Long): CartResponse {
        return cartService.getCart(id)
    }

    @PostMapping("/carts/{id}/add_recipe")
    fun addRecipeToCart(@PathVariable id: Long, @Valid @RequestBody request: AddRecipeRequest): CartResponse {
        return cartService.addRecipe(id, request.recipeId)
    }

    @DeleteMapping("/carts/{cartId}/recipes/{recipeId}")
    fun removeRecipeFromCart(@PathVariable cartId: Long, @PathVariable recipeId: Long): CartResponse {
        return cartService.removeRecipe(cartId, recipeId)
    }
} 