package co.morillas.repository

import co.morillas.core.domain.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository: JpaRepository<Recipe, Long>