package com.bassmeister.burgerworld.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class IngredientSerializer : JsonSerializer<Ingredient>() {

    override fun serialize(ingredient: Ingredient, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeFieldName(ingredient.id)
    }

}