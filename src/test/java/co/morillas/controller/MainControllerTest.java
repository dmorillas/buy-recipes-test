package co.morillas.controller;

import co.morillas.core.service.RecipeService;
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
}
