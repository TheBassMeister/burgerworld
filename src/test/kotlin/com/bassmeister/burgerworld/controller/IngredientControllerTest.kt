package com.bassmeister.burgerworld.controller

import com.bassmeister.burgerworld.model.Ingredient
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class IngredientControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun getAllIngredients() {
        mockMvc.perform(get("/ingredients")).andExpect(status().isOk).andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$", hasSize<Ingredient>(14)))
    }

    @Test
    fun getAllSauces() {
        mockMvc.perform(get("/ingredients?type=SAUCE")).andExpect(status().isOk).andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$", hasSize<Ingredient>(3)))
            .andExpect(jsonPath("$[0].name", `is`("Ketchup")))
            .andExpect(jsonPath("$[1].name", `is`("Mayo")))
            .andExpect(jsonPath("$[2].name", `is`("Chipotle")))
    }

    @Test
    fun getBeefPatty() {
        mockMvc.perform(get("/ingredients/B_PAT")).andExpect(status().isOk)
            .andExpect(jsonPath("$.name", `is`("Beef Patty")))
            .andExpect(jsonPath("$.cost", `is`(1.20)))
    }

    @Test
    fun getNonExistingIngredient() {
        mockMvc.perform(get("/ingredients/FOO")).andExpect(status().isNotFound)
    }
}