package com.bassmeister.burgerworld.validation

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.model.IngredientType
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Constraint(validatedBy = [BurgerValidator::class])
annotation class ValidBurger(
    val message: String = "A Burger must have one type of bun and at least one type of meat",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class BurgerValidator : ConstraintValidator<ValidBurger, Burger> {

    override fun isValid(burger: Burger, context: ConstraintValidatorContext?): Boolean {
        if (burger.ingredients.size >= 2) {
            var nrOfBuns = 0
            var nrOfMeat = 0
            burger.ingredients.forEach {
                if (it.key.type == IngredientType.BURGER) {
                    nrOfMeat += it.value
                } else if (it.key.type == IngredientType.BUN) {
                    nrOfBuns += it.value
                }
            }
            return nrOfBuns == 1 && nrOfMeat > 0
        }
        return false
    }

}