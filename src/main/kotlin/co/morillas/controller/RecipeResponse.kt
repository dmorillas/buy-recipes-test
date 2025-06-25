package co.morillas.controller

import co.morillas.core.domain.Recipe

data class RecipeResponse (
    val id: Long?,
    val name: String,
    val products: kotlin.collections.List<ProductResponse>
) {
    companion object {
        fun fromDomain(recipe: Recipe): RecipeResponse {
            val products = recipe.products
                .map { ProductResponse.fromDomain(it) }
                //.collect(Collectors.toList());

            return RecipeResponse(recipe.id, recipe.name, products);
        }
    }
}
