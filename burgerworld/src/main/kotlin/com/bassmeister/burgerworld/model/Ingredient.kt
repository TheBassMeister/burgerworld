package com.bassmeister.burgerworld.model

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class Ingredient(
    @Id val id: String,
    val name: String,
    val cost: Double,
    @Enumerated(EnumType.STRING)
    val type: IngredientType
)


enum class IngredientType {
    BUN,
    BURGER,
    VEGETABLE,
    SAUCE,
    OTHER
}