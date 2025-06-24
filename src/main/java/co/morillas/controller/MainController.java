package co.morillas.controller;

import co.morillas.core.service.CartService;
import co.morillas.core.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    private final CartService cartService;
    private final RecipeService recipeService;

    public MainController(RecipeService recipeService, CartService cartService) {
        this.cartService = cartService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public List<RecipeResponse> getAllRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/carts/{id}")
    public CartResponse getCart(@PathVariable Long id) {
        return cartService.getCart(id);
    }

    @PostMapping("/carts/{id}/add_recipe")
    public CartResponse addRecipeToCart(@PathVariable Long id, @Valid @RequestBody AddRecipeRequest request) {
        return cartService.addRecipe(id, request.getRecipeId());
    }

    @DeleteMapping("/carts/{cartId}/recipes/{recipeId}")
    public CartResponse removeRecipeFromCart(@PathVariable Long cartId, @PathVariable Long recipeId) {
        return cartService.removeRecipe(cartId, recipeId);
    }
} 