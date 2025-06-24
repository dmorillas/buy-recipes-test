package co.morillas.core.service;

import co.morillas.core.domain.Product;
import co.morillas.core.domain.Recipe;
import co.morillas.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    void getRecipes_ShouldReturnListOfRecipes() {
        Product product1 = new Product(1L, "product1", 10);
        Product product2 = new Product(2L, "product2", 20);
        Product product3 = new Product(3L, "product3", 30);

        Recipe recipe1 = new Recipe(1L, "recipe1", Arrays.asList(product1, product2));
        Recipe recipe2 = new Recipe(2L, "recipe2", Arrays.asList(product2, product3));
        List<Recipe> expectedRecipes = Arrays.asList(recipe1, recipe2);

        when(recipeRepository.findAll()).thenReturn(expectedRecipes);

        List<Recipe> result = recipeService.getRecipes();

        assertThat(expectedRecipes.size()).isEqualTo(result.size());
        assertThat(expectedRecipes).isEqualTo(result);
        verify(recipeRepository, times(1)).findAll();
    }
}
