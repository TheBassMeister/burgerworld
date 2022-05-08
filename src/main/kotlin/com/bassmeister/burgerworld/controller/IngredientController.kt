package com.bassmeister.burgerworld.controller

import com.bassmeister.burgerworld.model.Ingredient
import com.bassmeister.burgerworld.model.IngredientType
import com.bassmeister.burgerworld.repo.IngredientRepo
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ingredients", produces = [APPLICATION_JSON_VALUE])
class IngredientController(@Autowired val ingredientRepo: IngredientRepo) {

    private val logger = KotlinLogging.logger {}

    @CrossOrigin
    @GetMapping
    fun getAllIngredients(@RequestParam(required = false) type: String?): List<Ingredient> {
        return if (type == null) {
            logger.info { "Got a request to find all ingredients" }
            ingredientRepo.findAll().toList()
        } else {
            logger.info { "Got a request to find all ingredients of type $type" }
            return getIngredientsByType(type)
        }
    }

    fun getIngredientsByType(type: String): List<Ingredient> {
        return try {
            ingredientRepo.getIngredientByType(IngredientType.valueOf(type))
        } catch (ex: IllegalArgumentException) {
            logger.info { "There are no ingredients of type $type" }
            emptyList()
        }
    }

    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id: String): ResponseEntity<Ingredient> {
        logger.info { "Got a request to ingredient with id $id" }
        val ingredient = ingredientRepo.findById(id)
        return if (ingredient.isPresent) {
            logger.info { "Ingredient with id $id found" }
            ResponseEntity(ingredient.get(), HttpStatus.OK)
        } else {
            logger.info { "Ingredient with id $id does not exist" }
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

}