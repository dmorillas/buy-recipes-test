package co.morillas.core.service;

import co.morillas.controller.CartResponse;
import co.morillas.core.domain.Cart;
import co.morillas.core.domain.Product;
import co.morillas.core.domain.Recipe;
import co.morillas.core.exception.NotFoundException;
import co.morillas.repository.CartRepository;
import co.morillas.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RecipeRepository recipeRepository;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(cartRepository, recipeRepository);
    }

    @Test
    void getCart_ThrowsExceptionIfCartDoesNotExist() {
        Long cartId = 1L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartService.getCart(cartId)
        );

        assertThat(exception.getMessage()).isEqualTo("Cart with id 1 not found");
    }

    @Test
    void getCart_ReturnsCartWhenItExistsInDB() {
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);

        Cart expectedCart = new Cart(1L, 30, Arrays.asList(product1, product2));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));

        CartResponse cart = cartService.getCart(1L);

        assertThat(cart.getId()).isEqualTo(expectedCart.getId());
        assertThat(cart.getTotalInCents()).isEqualTo(expectedCart.getTotalInCents());
        assertThat(cart.getProducts()).hasSize(expectedCart.getProducts().size());
    }

    @Test
    void addRecipe_ThrowsExceptionIfCartDoesNotExist() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartService.addRecipe(1L, 0L)
        );

        assertThat(exception.getMessage()).isEqualTo("Cart with id 1 not found");
    }

    @Test
    void addRecipe_ThrowsExceptionIfRecipeDoesNotExist() {
        Cart expectedCart = new Cart(1L, 30, Collections.emptyList());

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartService.addRecipe(1L, 1L)
        );

        assertThat(exception.getMessage()).isEqualTo("Recipe with id 1 not found");
    }

    @Test
    void addRecipe_AddAllProductsInRecipeInTheCart() {
        Cart expectedCart = new Cart(1L, 0, new ArrayList<>());
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);
        Recipe recipe = new Recipe(1L, "recipe", Arrays.asList(product1, product2));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));
        when(cartRepository.save(any())).thenReturn(expectedCart);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        CartResponse cart = cartService.addRecipe(1L, 1L);

        assertThat(cart.getTotalInCents()).isEqualTo(30);
        assertThat(cart.getProducts()).hasSize(recipe.getProducts().size());
        assertThat(cart.getProducts().get(0).getId()).isEqualTo(recipe.getProducts().get(0).getId());
        assertThat(cart.getProducts().get(1).getId()).isEqualTo(recipe.getProducts().get(1).getId());
    }

    @Test
    void addRecipe_DoesNothingWhenCartAlreadyHasProduct() {
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);
        Cart expectedCart = new Cart(1L, 10, new ArrayList<>(List.of(product1)));
        Recipe recipe = new Recipe(1L, "recipe", Arrays.asList(product1, product2));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));
        when(cartRepository.save(any())).thenReturn(expectedCart);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        CartResponse cart = cartService.addRecipe(1L, 1L);

        assertThat(cart.getTotalInCents()).isEqualTo(30);
        assertThat(cart.getProducts()).hasSize(recipe.getProducts().size());
        assertThat(cart.getProducts().get(0).getId()).isEqualTo(recipe.getProducts().get(0).getId());
        assertThat(cart.getProducts().get(1).getId()).isEqualTo(recipe.getProducts().get(1).getId());
    }

    @Test
    void removeRecipe_ThrowsExceptionIfCartDoesNotExist() {
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartService.removeRecipe(1L, 0L)
        );

        assertThat(exception.getMessage()).isEqualTo("Cart with id 1 not found");
    }

    @Test
    void removeRecipe_ThrowsExceptionIfRecipeDoesNotExist() {
        Cart expectedCart = new Cart(1L, 30, Collections.emptyList());

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartService.removeRecipe(1L, 1L)
        );

        assertThat(exception.getMessage()).isEqualTo("Recipe with id 1 not found");
    }

    @Test
    void removeRecipe_RemoveProductsInRecipeFromTheCart() {
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);

        Cart expectedCart = new Cart(1L, 30, new ArrayList<>(Arrays.asList(product1, product2)));
        Recipe recipe = new Recipe(1L, "recipe", new ArrayList<>(Arrays.asList(product1, product2)));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));
        when(cartRepository.save(any())).thenReturn(expectedCart);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        CartResponse cart = cartService.removeRecipe(1L, 1L);

        assertThat(cart.getTotalInCents()).isEqualTo(0);
        assertThat(cart.getProducts()).hasSize(0);
    }

    @Test
    void removeRecipe_OnlyRemoveProductsAlreadyInCart() {
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);

        Cart expectedCart = new Cart(1L, 20, new ArrayList<>(List.of(product2)));
        Recipe recipe = new Recipe(1L, "recipe", new ArrayList<>(Arrays.asList(product1, product2)));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));
        when(cartRepository.save(any())).thenReturn(expectedCart);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        CartResponse cart = cartService.removeRecipe(1L, 1L);

        assertThat(cart.getTotalInCents()).isEqualTo(0);
        assertThat(cart.getProducts()).hasSize(0);
    }
}
