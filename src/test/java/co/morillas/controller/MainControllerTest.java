package co.morillas.controller;

import co.morillas.core.domain.Cart;
import co.morillas.core.domain.Product;
import co.morillas.core.service.RecipeService;
import co.morillas.repository.CartRepository;
import co.morillas.repository.ProductRepository;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MainControllerTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RecipeService recipeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void recipesReturns200AndAllListOfRecipesInDB() {
        when()
            .get("/recipes")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("$", hasSize(2))
            .body("[0].id", is(1))
            .body("[0].name", is("Spanish Omelette"))
            .body("[0].products", hasSize(5))
            .body("[1].id", is(2))
            .body("[1].name", is("Spaghetti Aglio e Olio"))
            .body("[1].products", hasSize(6))
        ;
    }

    @Test
    void cartsReturns404IfCartDoesNotExist() {
        when()
            .get("/carts/1")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", is("Car with id 1 not found"))
        ;
    }

    @Test
    void cartsReturns200AndExpectedCart() {
        Product product = new Product();
        product.setName("product");
        product.setPriceInCents(10);
        productRepository.save(product);

        Cart expectedCart = new Cart();
        expectedCart.addProduct(product);
        expectedCart = cartRepository.save(expectedCart);

        when()
            .get("/carts/1")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", is(expectedCart.getId().intValue()))
            .body("totalInCents", is(expectedCart.getTotalInCents()))
            .body("products", hasSize(expectedCart.getProducts().size()))
        ;

        cartRepository.deleteById(expectedCart.getId());
    }

    @Test
    void addRecipeReturns404IfCartDoesNotExist() {
        given()
            .contentType(ContentType.JSON)
            .body(new AddRecipeRequest(1L))
        .when()
            .post("/carts/1/add_recipe")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", is("Car with id 1 not found"))
        ;
    }

    @Test
    void addRecipeReturns404IfRecipeDoesNotExist() {
        Cart expectedCart = new Cart();
        expectedCart = cartRepository.save(expectedCart);

        given()
            .contentType(ContentType.JSON)
            .body(new AddRecipeRequest(4L))
        .when()
            .post("/carts/1/add_recipe")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", is("Recipe with id 4 not found"))
        ;

        cartRepository.deleteById(expectedCart.getId());
    }

    @Test
    void addRecipeReturns200AndCartWithRecipeProducts() {
        Cart expectedCart = new Cart();
        expectedCart = cartRepository.save(expectedCart);

        given()
            .contentType(ContentType.JSON)
            .body(new AddRecipeRequest(1L))
        .when()
            .post("/carts/1/add_recipe")
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", is(expectedCart.getId().intValue()))
            .body("totalInCents", is(150))
            .body("products", hasSize(5))
        ;

        cartRepository.deleteById(expectedCart.getId());
    }
}
