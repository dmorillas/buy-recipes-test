package co.morillas.controller;

import co.morillas.core.domain.Cart;
import co.morillas.core.domain.Recipe;
import co.morillas.core.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    private final RecipeService recipeService;

    public MainController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeService.getRecipes();
    }

    @GetMapping("/carts/{id}")
    public Cart getCart(@PathVariable Long id) {
        return new Cart();
    }

    @PostMapping("/carts/{id}/add_recipe")
    public Cart addRecipeToCart(@PathVariable Long id, @Valid @RequestBody AddRecipeRequest request) {
        return new Cart();
    }

    @DeleteMapping("/carts/{cartId}/recipes/{recipeId}")
    public void removeRecipeFromCart(@PathVariable Long cartId, @PathVariable Long recipeId) {

    }
} 