package com.bassmeister.burgerworld

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.model.Ingredient
import com.bassmeister.burgerworld.repo.BurgerRepo
import com.bassmeister.burgerworld.repo.IngredientRepo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsMapContaining
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.validation.ConstraintViolationException


@DataJpaTest
class BurgerTest(@Autowired val burgerRepo: BurgerRepo, @Autowired val ingredientRepo: IngredientRepo) {

    private val validationErrMessage = "A Burger must have one type of bun and at least one type of meat"

    @Test
    fun getChickenBaconMaster() {
        val burger = burgerRepo.getBurgerByName("Chicken Bacon Master")
        assertNotNull(burger, "Could not find Chicken Bacon Master")
        assertEquals(5.94, burger.getTotalCost(), 0.01)
        val ingredients = burger.ingredients
        assertThat(ingredients, IsMapContaining.hasEntry(ingredientRepo.findById("REG_BUN").get(), 1))
        assertThat(ingredients, IsMapContaining.hasEntry(ingredientRepo.findById("CH_PAT").get(), 1))
        assertThat(ingredients, IsMapContaining.hasEntry(ingredientRepo.findById("LETC").get(), 2))
        assertThat(ingredients, IsMapContaining.hasEntry(ingredientRepo.findById("CHIPO").get(), 3))
        assertThat(ingredients, IsMapContaining.hasEntry(ingredientRepo.findById("BAC").get(), 2))
    }

    @Test
    fun addValidBurger() {
        val ingredients = HashMap<Ingredient, Int>();
        ingredients[ingredientRepo.findById("SES_BUN").get()] = 1
        ingredients[ingredientRepo.findById("B_PAT").get()] = 1
        ingredients[ingredientRepo.findById("CH_PAT").get()] = 1
        ingredients[ingredientRepo.findById("TOMA").get()] = 3
        ingredients[ingredientRepo.findById("KETCH").get()] = 1
        ingredients[ingredientRepo.findById("MAYO").get()] = 1
        ingredients[ingredientRepo.findById("PIC").get()] = 5
        ingredients[ingredientRepo.findById("BAC").get()] = 2

        val burger = Burger(0, "McTest", true, ingredients)
        burgerRepo.save(burger)

        val afterSave = burgerRepo.getBurgerByName("McTest")
        assertNotNull(afterSave, "New Burger was not persisted")
        assertTrue(afterSave.isCustom);
        assertEquals(8.53, afterSave.getTotalCost(), 0.01)
    }

    @Test
    fun addBurgerWithTooManyBuns() {
        val ingredients = HashMap<Ingredient, Int>();
        ingredients[ingredientRepo.findById("SES_BUN").get()] = 2
        ingredients[ingredientRepo.findById("B_PAT").get()] = 1
        ingredients[ingredientRepo.findById("TOMA").get()] = 3
        ingredients[ingredientRepo.findById("MAYO").get()] = 1
        ingredients[ingredientRepo.findById("PIC").get()] = 5

        val burger = Burger(0, "TwoBuns", true, ingredients)
        try {
            burgerRepo.save(burger)
            fail("Burgers with more than one bun are not allowed")
        } catch (e: ConstraintViolationException) {
            val constraintMessage = e.constraintViolations.iterator().next().message
            assertEquals(validationErrMessage, constraintMessage)
        }
    }

    @Test
    fun addBurgerWithoutBuns() {
        val ingredients = HashMap<Ingredient, Int>();
        ingredients[ingredientRepo.findById("B_PAT").get()] = 1
        ingredients[ingredientRepo.findById("TOMA").get()] = 3
        ingredients[ingredientRepo.findById("MAYO").get()] = 1
        ingredients[ingredientRepo.findById("PIC").get()] = 5

        val burger = Burger(0, "TheBunLess", true, ingredients)
        try {
            burgerRepo.save(burger)
            fail("Burgers must have a bun")
        } catch (e: ConstraintViolationException) {
            val constraintMessage = e.constraintViolations.iterator().next().message
            assertEquals(validationErrMessage, constraintMessage)
        }
    }

    @Test
    fun addBurgerWithoutPatty() {
        val ingredients = HashMap<Ingredient, Int>();
        ingredients[ingredientRepo.findById("REG_BUN").get()] = 1
        ingredients[ingredientRepo.findById("TOMA").get()] = 3
        ingredients[ingredientRepo.findById("MAYO").get()] = 1
        ingredients[ingredientRepo.findById("BAC").get()] = 3

        val burger = Burger(0, "PattyLess", true, ingredients)
        try {
            burgerRepo.save(burger)
            fail("Burgers must have at least one patty")
        } catch (e: ConstraintViolationException) {
            val constraintMessage = e.constraintViolations.iterator().next().message
            assertEquals(validationErrMessage, constraintMessage)
        }
    }

}