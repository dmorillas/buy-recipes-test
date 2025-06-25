package co.morillas.controller

import jakarta.validation.constraints.NotNull

data class AddRecipeRequest (
    @NotNull(message = "recipe name can't be null or empty")
    val recipeId: Long
)