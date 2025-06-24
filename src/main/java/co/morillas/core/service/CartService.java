package co.morillas.core.service;

import co.morillas.core.domain.Cart;
import co.morillas.core.domain.Recipe;
import co.morillas.core.exception.NotFoundException;
import co.morillas.repository.CartRepository;
import co.morillas.repository.RecipeRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final RecipeRepository recipeRepository;

    public CartService(CartRepository cartRepository, RecipeRepository recipeRepository) {
        this.cartRepository = cartRepository;
        this.recipeRepository = recipeRepository;
    }

    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Cart with id " + id + " not found"));
    }

    public Cart addRecipe(Long cartId, Long recipeId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart with id " + cartId + " not found"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new NotFoundException("Recipe with id " + recipeId + " not found"));

        recipe.getProducts().forEach(cart::addProduct);
        cart = cartRepository.save(cart);

        return cart;
    }
}
