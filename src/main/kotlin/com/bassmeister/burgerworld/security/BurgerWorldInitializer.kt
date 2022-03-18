package com.bassmeister.burgerworld.security

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.model.Ingredient
import com.bassmeister.burgerworld.model.User
import com.bassmeister.burgerworld.repo.BurgerRepo
import com.bassmeister.burgerworld.repo.IngredientRepo
import com.bassmeister.burgerworld.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@Profile("dev")
class BurgerWorldInitializer {

    @Bean
    fun createTestUser(@Autowired userRepo: UserRepo, @Autowired pwEncoder: PasswordEncoder): CommandLineRunner {
        return CommandLineRunner {
            val adminUser = User(
                0,
                "root",
                pwEncoder.encode("pass"),
                true,
                listOf("ROLE_USER", "ROLE_ADMIN")
            )
            userRepo.save(adminUser)
            val regularUser = User(
                0,
                "Ronald",
                pwEncoder.encode("King Burger"),
                true,
                listOf("ROLE_USER")
            )
            userRepo.save(regularUser)
        }
    }

    @Bean
    @Autowired
    fun createBurgers(burgerRepo: BurgerRepo, ingredientRepo: IngredientRepo): CommandLineRunner {
        return CommandLineRunner {
            val cbm = burgerRepo.getBurgerByName("Chicken Bacon Master")
            if (!cbm.isPresent) {
                val cbmIngredients = HashMap<Ingredient, Int>()
                cbmIngredients[ingredientRepo.findById("REG_BUN").get()] = 1
                cbmIngredients[ingredientRepo.findById("CH_PAT").get()] = 1
                cbmIngredients[ingredientRepo.findById("LETC").get()] = 2
                cbmIngredients[ingredientRepo.findById("CHIPO").get()] = 3
                cbmIngredients[ingredientRepo.findById("BAC").get()] = 2
                val cbmBurger = Burger(0, "Chicken Bacon Master", false, cbmIngredients)
                burgerRepo.save(cbmBurger)
            }

            val veg = burgerRepo.getBurgerByName("The Vegetarian")
            if (!veg.isPresent) {
                val vegIngredients = HashMap<Ingredient, Int>()
                vegIngredients[ingredientRepo.findById("GLU_BUN").get()] = 1
                vegIngredients[ingredientRepo.findById("VEG_PAT").get()] = 1
                vegIngredients[ingredientRepo.findById("LETC").get()] = 3
                vegIngredients[ingredientRepo.findById("MAYO").get()] = 3
                val vegBurger = Burger(0, "The Vegetarian", false, vegIngredients)
                burgerRepo.save(vegBurger)
            }

            val classic = burgerRepo.getBurgerByName("The Classic")
            if (!veg.isPresent) {
                val vegIngredients = HashMap<Ingredient, Int>()
                vegIngredients[ingredientRepo.findById("SES_BUN").get()] = 1
                vegIngredients[ingredientRepo.findById("B_PAT").get()] = 2
                vegIngredients[ingredientRepo.findById("LETC").get()] = 3
                vegIngredients[ingredientRepo.findById("TOMA").get()] = 4
                vegIngredients[ingredientRepo.findById("CHEE").get()] = 2
                val classicBurger = Burger(0, "The Classic", false, vegIngredients)
                burgerRepo.save(classicBurger)
            }

        }

    }
}