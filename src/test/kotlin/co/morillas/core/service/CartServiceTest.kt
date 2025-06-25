package co.morillas.core.service

import co.morillas.core.domain.Cart
import co.morillas.core.domain.Product
import co.morillas.core.domain.Recipe
import co.morillas.core.exception.NotFoundException
import co.morillas.repository.CartRepository
import co.morillas.repository.RecipeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.*
import java.util.*

class CartServiceTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    private lateinit var cartService: CartService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        cartService = CartService(cartRepository, recipeRepository)
    }

    @Test
    fun `should throw exception if cart id does not exist`() {
        val cartId = 1L
        `when`(cartRepository.findById(cartId)).thenReturn(Optional.empty())

        val exception = assertThrows(
                NotFoundException::class.java,
                { cartService.getCart(cartId) }
        )

        assertThat(exception.message).isEqualTo("Cart with id 1 not found")
    }

    @Test
    fun `whould return the expected cart information`() {
        val product1 = Product(1L, "product1", 10)
        val product2 = Product(2L, "product2", 20)

        val expectedCart = Cart(1L, 30, mutableListOf(product1, product2))

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))

        val cart = cartService.getCart(1L)

        assertThat(cart.id).isEqualTo(expectedCart.id)
        assertThat(cart.totalInCents).isEqualTo(expectedCart.totalInCents)
        assertThat(cart.products).hasSize(expectedCart.products.size)
    }

    @Test
    fun `addRecipe should throw exception if cart does not exist`() {
        `when`(cartRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows(
                NotFoundException::class.java,
                { cartService.addRecipe(1L, 0L) }
        )

        assertThat(exception.message).isEqualTo("Cart with id 1 not found")
    }

    @Test
    fun `addRecipe should throw exception if recipe does not exist`() {
        val expectedCart = Cart(1L, 30, mutableListOf<Product>())

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))

        val exception = assertThrows(
                NotFoundException::class.java,
                { cartService.addRecipe(1L, 1L) }
        )

        assertThat(exception.message).isEqualTo("Recipe with id 1 not found")
    }

    @Test
    fun `addRecipe should add all products in recipe to the cart`() {
        val expectedCart = Cart(1L, 0, mutableListOf())
        val product1 = Product(1L, "product1", 10)
        val product2 = Product(2L, "product2", 20)
        val recipe = Recipe(1L, "recipe", listOf(product1, product2))

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))
        `when`(cartRepository.save(any())).thenReturn(expectedCart)
        `when`(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe))

        val cart = cartService.addRecipe(1L, 1L)

        assertThat(cart.totalInCents).isEqualTo(30)
        assertThat(cart.products).hasSize(recipe.products.size)
        assertThat(cart.products[0].id).isEqualTo(recipe.products[0].id)
        assertThat(cart.products[1].id).isEqualTo(recipe.products[1].id)
    }

    @Test
    fun `addRecipe does nothing when cart already has product`() {
        val product1 = Product(1L, "product1", 10)
        val product2 = Product(2L, "product2", 20)
        val expectedCart = Cart(1L, 10, mutableListOf(product1))
        val recipe = Recipe(1L, "recipe", listOf(product1, product2))

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))
        `when`(cartRepository.save(any())).thenReturn(expectedCart)
        `when`(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe))

        val cart = cartService.addRecipe(1L, 1L)

        assertThat(cart.totalInCents).isEqualTo(30)
        assertThat(cart.products).hasSize(recipe.products.size)
        assertThat(cart.products[0].id).isEqualTo(recipe.products[0].id)
        assertThat(cart.products[1].id).isEqualTo(recipe.products[1].id)
    }

    @Test
    fun `removeRecipe should throw exception if cart does not exist`() {
        `when`(cartRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows(
                NotFoundException::class.java,
                { cartService.removeRecipe(1L, 0L) }
        )

        assertThat(exception.message).isEqualTo("Cart with id 1 not found")
    }

    @Test
    fun `removeRecipe should exception if recipe does not exist`() {
        val expectedCart = Cart(1L, 30, mutableListOf<Product>())

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))

        val exception = assertThrows(
                NotFoundException::class.java,
                { cartService.removeRecipe(1L, 1L) }
        )

        assertThat(exception.message).isEqualTo("Recipe with id 1 not found")
    }

    @Test
    fun `removeRecipe removes products in recipe from the cart`() {
        val product1 = Product(1L, "product1", 10)
        val product2 = Product(2L, "product2", 20)

        val expectedCart = Cart(1L, 30, mutableListOf(product1, product2))
        val recipe = Recipe(1L, "recipe", mutableListOf(product1, product2))

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))
        `when`(cartRepository.save(any())).thenReturn(expectedCart)
        `when`(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe))

        val cart = cartService.removeRecipe(1L, 1L)

        assertThat(cart.totalInCents).isEqualTo(0)
        assertThat(cart.products).isEmpty()
    }

    @Test
    fun `removeRecipe only removes products already in cart`() {
        val product1 = Product(1L, "product1", 10)
        val product2 = Product(2L, "product2", 20)

        val expectedCart = Cart(1L, 20, mutableListOf(product2))
        val recipe = Recipe(1L, "recipe", mutableListOf(product1, product2))

        `when`(cartRepository.findById(1L)).thenReturn(Optional.of(expectedCart))
        `when`(cartRepository.save(any())).thenReturn(expectedCart)
        `when`(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe))

        val cart = cartService.removeRecipe(1L, 1L)

        assertThat(cart.totalInCents).isEqualTo(0)
        assertThat(cart.products).isEmpty()
    }
}
