package com.bassmeister.burgerworld.model

import com.bassmeister.burgerworld.repo.IngredientRepo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class IngredientTest(@Autowired val ingredientRepo: IngredientRepo) {

    @Test
    fun getAllIngredients() {
        val ingredients = ingredientRepo.findAll().toList()
        assertEquals(14, ingredients.size, "Not all 14 expected ingredients were found")
    }

    @ParameterizedTest
    @CsvSource(value = ["BUN,3", "BURGER,3", "VEGETABLE,2", "SAUCE,3", "OTHER,3"])
    fun getAllBuns(ingredientType: String, expectedAmount: Int) {
        val type = IngredientType.valueOf(ingredientType)
        val ingredients = ingredientRepo.getIngredientByType(type)
        val allOfCorrectType = ingredients.stream().allMatch { it.type == type }
        assertTrue(allOfCorrectType, "Not all ingredients were of type $ingredientType")
        assertEquals(expectedAmount, ingredients.size, "Did not receive all $expectedAmount ingredient")
    }

}