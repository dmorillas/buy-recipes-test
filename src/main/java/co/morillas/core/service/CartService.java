package co.morillas.core.service;

import co.morillas.core.domain.Cart;
import co.morillas.core.exception.NotFoundException;
import co.morillas.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow(() -> new NotFoundException("Car with id " + id + " not found"));
    }
}
