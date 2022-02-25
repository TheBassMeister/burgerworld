package com.bassmeister.burgerworld.repo

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.model.Ingredient
import com.bassmeister.burgerworld.model.IngredientType
import org.springframework.data.repository.CrudRepository

interface IngredientRepo : CrudRepository<Ingredient, String> {

    fun getIngredientByType(type: IngredientType): List<Ingredient>
}

interface BurgerRepo : CrudRepository<Burger, String> {

    fun getBurgerByName(name: String): Burger
}