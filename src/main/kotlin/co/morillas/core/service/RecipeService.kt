package co.morillas.core.service

import co.morillas.controller.RecipeResponse
import co.morillas.repository.RecipeRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(private val recipeRepository: RecipeRepository) {

    fun getRecipes(): List<RecipeResponse> {
        val recipes = recipeRepository.findAll()

        return recipes.map(RecipeResponse::fromDomain)
            //.collect(Collectors.toList())
    }
}
