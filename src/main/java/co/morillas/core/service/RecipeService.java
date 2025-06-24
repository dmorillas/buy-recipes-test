package co.morillas.core.service;

import co.morillas.controller.RecipeResponse;
import co.morillas.core.domain.Recipe;
import co.morillas.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeResponse> getRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();

        return recipes.stream()
                .map(RecipeResponse::fromDomain)
                .collect(Collectors.toList());
    }
}
