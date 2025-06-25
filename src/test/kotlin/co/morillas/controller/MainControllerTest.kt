package co.morillas.controller

import co.morillas.core.domain.Cart
import co.morillas.core.domain.Product
import co.morillas.core.service.RecipeService
import co.morillas.repository.CartRepository
import co.morillas.repository.ProductRepository
import io.restassured.RestAssured.given
import io.restassured.RestAssured.`when`
import io.restassured.http.ContentType
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MainControllerTest {

    @Autowired
    private lateinit var cartRepository: CartRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var recipeService: RecipeService

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Test
    fun `when retrieven recipes it returns 200 and the list of recipes in database`() {
        `when`()
            .get("/recipes")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize<Any>(2))
            .body("[0].id", `is`(1))
            .body("[0].name", `is`("Spanish Omelette"))
            .body("[0].products", hasSize<Any>(5))
            .body("[1].id", `is`(2))
            .body("[1].name", `is`("Spaghetti Aglio e Olio"))
            .body("[1].products", hasSize<Any>(6))
        ;
    }

    @Test
    fun `when retrieving a cart it return 404 if the cart does not exist`() {
        `when`()
            .get("/carts/1")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", `is`("Cart with id 1 not found"))
    }

    @Test
    fun `when retrieving a cart it returns 200 and the expected cart when it exists in database`() {
        val product = Product(name = "product", priceInCents = 10)
        productRepository.save(product)

        var expectedCart = Cart()
        expectedCart.addProduct(product)
        expectedCart = cartRepository.save(expectedCart)

        `when`()
            .get("/carts/1")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", `is`(expectedCart.id?.toInt()))
            .body("totalInCents", `is`(expectedCart.totalInCents))
            .body("products", hasSize<Any>(expectedCart.products.size))

        cartRepository.deleteById(expectedCart.id!!)
    }

    @Test
    fun `when adding a recipe to a cart it returns 404 if the cart does not exist`() {
        given()
            .contentType(ContentType.JSON)
            .body(AddRecipeRequest(1L))
        .`when`()
            .post("/carts/1/add_recipe")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", `is`("Cart with id 1 not found"))
    }

    @Test
    fun `when adding a recipe to a cart it returns 404 if the recipe does not exist`() {
        var expectedCart = Cart()
        expectedCart = cartRepository.save(expectedCart)

        given()
            .contentType(ContentType.JSON)
            .body(AddRecipeRequest(4L))
        .`when`()
            .post("/carts/1/add_recipe")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", `is`("Recipe with id 4 not found"))

        cartRepository.deleteById(expectedCart.id!!)
    }

    @Test
    fun `when adding a recipe to a cart it returns 200 and the cart with the products added`() {
        var expectedCart = Cart()
        expectedCart = cartRepository.save(expectedCart)

        given()
            .contentType(ContentType.JSON)
            .body(AddRecipeRequest(1L))
        .`when`()
            .post("/carts/1/add_recipe")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", `is`(expectedCart.id?.toInt()))
            .body("totalInCents", `is`(150))
            .body("products", hasSize<Any>(5))

        cartRepository.deleteById(expectedCart.id!!)
    }

    @Test
    fun `when removing a recipe to a cart it returns 404 if the cart does not exist`() {
        `when`()
            .delete("/carts/1/recipes/1")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", `is`("Cart with id 1 not found"))
    }

    @Test
    fun `when removing a recipe to a cart it returns 404 if the recipe does not exist`() {
        var expectedCart = Cart()
        expectedCart = cartRepository.save(expectedCart)

        `when`()
            .delete("/carts/1/recipes/7")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", `is`("Recipe with id 7 not found"))

        cartRepository.deleteById(expectedCart.id!!)
    }

    @Test
    fun `when removing a recipe to a cart it returns 200 and the cart with the products removed`() {
        val oliveOil = productRepository.findById(1L).get()
        val potato = productRepository.findById(2L).get()
        val eggs = productRepository.findById(3L).get()
        val onion = productRepository.findById(4L).get()
        val salt = productRepository.findById(5L).get()
        val garlic = productRepository.findById(7L).get()

        var expectedCart = Cart()
        expectedCart.addProduct(oliveOil)
        expectedCart.addProduct(potato)
        expectedCart.addProduct(eggs)
        expectedCart.addProduct(onion)
        expectedCart.addProduct(salt)
        expectedCart.addProduct(garlic)
        expectedCart = cartRepository.save(expectedCart)

        `when`()
                .delete("/carts/1/recipes/1")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", `is`(expectedCart.id?.toInt()))
            .body("totalInCents", `is`(70))
            .body("products", hasSize<Any>(1))
            .body("products[0].id", `is`(7))

        cartRepository.deleteById(expectedCart.id!!)
    }

    @Test
    fun `when adding a cart should return 200 and the new cart information`() {
        `when`()
            .post("/carts")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("totalInCents", `is`(0))
            .body("products", hasSize<Any>(0))
    }

    @Test
    fun `when deleting a cart should return 404 if cart does not exist`() {
        `when`()
            .delete("/carts/1000")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", `is`("Cart with id 1000 not found"))
    }

    @Test
    fun `when deleting a cart should return 200 and delete the cart from repository`() {
        var expectedCart = Cart()
        expectedCart = cartRepository.save(expectedCart)

        `when`()
            .delete("/carts/${expectedCart.id}")
        .then()
            .statusCode(HttpStatus.SC_OK)

        assertThat(cartRepository.findById(expectedCart.id)).isEmpty()
    }
}
