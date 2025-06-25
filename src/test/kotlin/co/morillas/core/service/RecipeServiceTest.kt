package co.morillas.core.service

import co.morillas.core.domain.Product
import co.morillas.core.domain.Recipe
import co.morillas.repository.RecipeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.*

class RecipeServiceTest {

    @Mock
    private lateinit var recipeRepository: RecipeRepository

    private lateinit var recipeService: RecipeService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        recipeService = RecipeService(recipeRepository)
    }

    @Test
    fun `getRecipes should return list of recipes`() {
        val product1 = Product(1L, "product1", 10)
        val product2 = Product(2L, "product2", 20)
        val product3 = Product(3L, "product3", 30)

        val recipe1 = Recipe(1L, "recipe1", listOf(product1, product2))
        val recipe2 = Recipe(2L, "recipe2", listOf(product2, product3))
        val expectedRecipes = listOf(recipe1, recipe2)

        `when`(recipeRepository.findAll()).thenReturn(expectedRecipes)

        val result = recipeService.getRecipes()

        assertThat(expectedRecipes.size).isEqualTo(result.size)
        assertThat(expectedRecipes[0].id).isEqualTo(result[0].id)
        assertThat(expectedRecipes[1].id).isEqualTo(result[1].id)
        verify(recipeRepository, times(1)).findAll()
    }
}
