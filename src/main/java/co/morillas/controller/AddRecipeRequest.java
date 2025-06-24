package co.morillas.controller;

import jakarta.validation.constraints.NotNull;

public class AddRecipeRequest {
    @NotNull(message = "recipe name can't be null or empty")
    private Long recipeId;

    public AddRecipeRequest() {}

    public AddRecipeRequest(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}