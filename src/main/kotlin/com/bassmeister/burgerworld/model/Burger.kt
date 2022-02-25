package com.bassmeister.burgerworld.model

import com.bassmeister.burgerworld.validation.ValidBurger
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@ValidBurger
data class Burger(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @NotNull
    val name: String,
    @Column(columnDefinition = "boolean default false")
    val isCustom: Boolean,
    @ElementCollection
    @CollectionTable(
        name = "burger_ingredients",
        joinColumns = [JoinColumn(name = "burger_id", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "ingredient_id")
    @Column(name = "amount")
    val ingredients: Map<Ingredient, Int>,
) {

    fun getTotalCost(): Double {
        var cost = 2.30 //2.30 is base price
        ingredients.forEach { ingredient -> cost += (ingredient.key.cost * ingredient.value) }
        return cost
    }

}