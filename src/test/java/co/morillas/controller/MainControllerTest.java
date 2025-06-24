package co.morillas.controller;

import co.morillas.core.domain.Cart;
import co.morillas.core.domain.Product;
import co.morillas.core.service.RecipeService;
import co.morillas.repository.CartRepository;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MainControllerTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RecipeService recipeService;

    @Test
    void returns200AndAllListOfRecipesInDB() {
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
    void returns404IfCartDoesNotExist() {
        when()
            .get("/carts/1")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND)
            .body("message", is("Car with id 1 not found"))
        ;
    }

    @Test
    void returns200AndExpectedCart() {
        Product product = new Product();
        product.setName("product");
        product.setPriceInCents(10);
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
}
