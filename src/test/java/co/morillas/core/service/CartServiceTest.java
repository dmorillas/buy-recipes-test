package co.morillas.core.service;

import co.morillas.core.domain.Cart;
import co.morillas.core.domain.Product;
import co.morillas.core.exception.NotFoundException;
import co.morillas.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService(cartRepository);
    }

    @Test
    void getCart_ThrowsExceptionIfCartDoesNotExist() {
        Long cartId = 1L;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> cartService.getCart(cartId)
        );

        assertThat(exception.getMessage()).isEqualTo("Car with id 1 not found");
    }

    @Test
    void getCart_ReturnsCartWhenItExistsInDB() {
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);

        Cart expectedCart = new Cart(1L, 30, Arrays.asList(product1, product2));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart));

        Cart cart = cartService.getCart(1L);

        assertThat(cart.getId()).isEqualTo(expectedCart.getId());
        assertThat(cart.getTotalInCents()).isEqualTo(expectedCart.getTotalInCents());
        assertThat(cart.getProducts()).hasSize(expectedCart.getProducts().size());
    }
}
