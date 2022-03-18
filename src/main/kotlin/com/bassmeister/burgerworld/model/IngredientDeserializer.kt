package com.bassmeister.burgerworld.model

import com.bassmeister.burgerworld.repo.IngredientRepo
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import org.springframework.beans.factory.annotation.Autowired

class IngredientDeserializer(@Autowired val ingredientRepo: IngredientRepo) : KeyDeserializer() {


    override fun deserializeKey(ingredientString: String, desContext: DeserializationContext?): Ingredient {
        return ingredientRepo.findById(ingredientString).get()
    }


}