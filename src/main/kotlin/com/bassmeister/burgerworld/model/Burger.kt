package com.bassmeister.burgerworld.model

import com.bassmeister.burgerworld.validation.ValidBurger
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@ValidBurger
data class Burger(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val id: Long,
    @NotNull @Column(unique = true)
    val name: String,
    @Column(columnDefinition = "boolean default false") @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var isCustom: Boolean,
    @ElementCollection
    @CollectionTable(
        name = "burger_ingredients",
        joinColumns = [JoinColumn(name = "burger_id", referencedColumnName = "id")]
    )
    @MapKeyColumn(name = "ingredient_id")
    @Column(name = "amount")
    @JsonSerialize(keyUsing = IngredientSerializer::class) @JsonDeserialize(keyUsing = IngredientDeserializer::class)
    val ingredients: Map<Ingredient, Int>,
) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    fun getTotalCost(): BigDecimal {
        var cost = BigDecimal.valueOf(2.30) //2.30 is base price
        ingredients.forEach { ingredient -> cost += BigDecimal.valueOf(ingredient.key.cost * ingredient.value) }
        return cost.setScale(2, RoundingMode.HALF_UP)
    }

}