package com.bassmeister.burgerworld.repo

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.model.Ingredient
import com.bassmeister.burgerworld.model.IngredientType
import org.springframework.data.repository.CrudRepository
import java.util.*

interface IngredientRepo : CrudRepository<Ingredient, String> {

    fun getIngredientByType(type: IngredientType): List<Ingredient>
}

interface BurgerRepo : CrudRepository<Burger, Long> {

    fun getBurgerByName(name: String): Optional<Burger>
}