package com.bassmeister.burgerworld.controller

import com.bassmeister.burgerworld.model.Ingredient
import com.bassmeister.burgerworld.model.IngredientType
import com.bassmeister.burgerworld.repo.IngredientRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ingredients", produces = [APPLICATION_JSON_VALUE])
class IngredientController(@Autowired val ingredientRepo: IngredientRepo) {

    @GetMapping
    fun getAllIngredients(@RequestParam(required = false) type: String?): List<Ingredient> {
        return if (type == null) {
            ingredientRepo.findAll().toList()
        } else {
            return getIngredientsByType(type)
        }
    }

    fun getIngredientsByType(type: String): List<Ingredient> {
        return try {
            ingredientRepo.getIngredientByType(IngredientType.valueOf(type))
        } catch (ex: IllegalArgumentException) {
            emptyList()
        }
    }

    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id: String): ResponseEntity<Ingredient> {
        val ingredient = ingredientRepo.findById(id)
        return if (ingredient.isPresent) {
            ResponseEntity(ingredient.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

}