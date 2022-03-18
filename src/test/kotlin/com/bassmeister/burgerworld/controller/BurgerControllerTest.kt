package com.bassmeister.burgerworld.controller

import com.bassmeister.burgerworld.model.Ingredient
import com.jayway.jsonpath.JsonPath
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BurgerControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun getAllBurgers() {
        mockMvc.perform(get("/burgers")).andExpect(status().isOk).andExpect(
            jsonPath("$").isArray
        ).andExpect(status().isOk)
    }

    @Test
    fun getAllStandardBurgers() {
        mockMvc.perform(get("/burgers?isCustom=false"))
            .andExpect(status().isOk).andExpect(
                jsonPath("$").isArray
            )
            .andExpect(jsonPath("$", hasSize<Ingredient>(3)))
            .andExpect(jsonPath("$.*.isCustom", everyItem(`is`(false))))
    }

    @Test
    fun getTheClassic() {
        mockMvc.perform(get("/burgers/The Classic")).andExpect(status().isOk)
            .andExpect(jsonPath("$.name", `is`("The Classic")))
            .andExpect(jsonPath("$.ingredients", aMapWithSize<Ingredient, Int>(5)))
            .andExpect(jsonPath("$.isCustom", `is`(false)))
            .andExpect(jsonPath("$.totalCost", `is`(6.8)))
    }

    @Test
    @WithMockUser
    fun createNewBurger() {
        mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"RestTest Burger\"," +
                        "\"ingredients\": {" +
                        "   \"REG_BUN\": 1," +
                        "   \"BAC\": 2," +
                        "   \"LETC\": 2," +
                        "   \"CHIPO\": 3," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isCreated).andExpect(jsonPath("$.name", `is`("RestTest Burger")))
            .andExpect(jsonPath("$.ingredients", aMapWithSize<Ingredient, Int>(5)))
            .andExpect(jsonPath("$.ingredients", hasEntry("REG_BUN", 1)))
            .andExpect(jsonPath("$.ingredients", hasEntry("BAC", 2)))
            .andExpect(jsonPath("$.ingredients", hasEntry("LETC", 2)))
            .andExpect(jsonPath("$.ingredients", hasEntry("CHIPO", 3)))
            .andExpect(jsonPath("$.ingredients", hasEntry("CH_PAT", 1)))
            .andExpect(jsonPath("$.isCustom", `is`(true)))
            .andExpect(jsonPath("$.totalCost", `is`(5.94)))

    }

    @Test
    @WithMockUser
    fun createBurgerWithoutBun() {
        mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"The Bunless\"," +
                        "\"ingredients\": {" +
                        "   \"BAC\": 2," +
                        "   \"LETC\": 2," +
                        "   \"CHIPO\": 3," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isForbidden)
            .andExpect(jsonPath("$", `is`("A Burger must have one type of bun and at least one type of meat")))
    }

    @Test
    @WithMockUser
    fun createBurgerWithExistingName() {
        val burgerName = "The Classic"
        mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"$burgerName\"," +
                        "\"ingredients\": {" +
                        "   \"REG_BUN\": 1," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isForbidden)
            .andExpect(jsonPath("$", `is`("Burger with name $burgerName already exists")))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun deleteNonCustomBurger() {
        mockMvc.perform(delete("/burgers/1")).andExpect(status().isForbidden)
            .andExpect(jsonPath("$", `is`("Only custom Burgers can be deleted")))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun deleteCustomBurger() {
        val postResult = mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"Will be deleted burger\"," +
                        "\"ingredients\": {" +
                        "   \"REG_BUN\": 1," +
                        "   \"BAC\": 2," +
                        "   \"LETC\": 2," +
                        "   \"CHIPO\": 3," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isCreated).andReturn()
        val newBurgerId = JsonPath.read<Integer>(postResult.response.contentAsString, "$.id")
        mockMvc.perform(delete("/burgers/$newBurgerId")).andExpect(status().isOk)

        val allBurgersAfterDelete = mockMvc.perform(get("/burgers")).andReturn()
        assertFalse(
            allBurgersAfterDelete.response.contentAsString.contains("Will be deleted burger"),
            "Burger was not deleted"
        )
    }

    @Test
    @WithMockUser(roles = ["TEST_USER"])
    fun onlyUsersCanCreateBurgers() {
        mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"The Forbidden Burger\"," +
                        "\"ingredients\": {" +
                        "   \"REG_BUN\": 1," +
                        "   \"BAC\": 2," +
                        "   \"LETC\": 2," +
                        "   \"CHIPO\": 3," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isForbidden)
    }

    @Test
    fun mustBeLoggedInToCreateBurgers() {
        mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"The Unlogged one\"," +
                        "\"ingredients\": {" +
                        "   \"REG_BUN\": 1," +
                        "   \"BAC\": 2," +
                        "   \"LETC\": 2," +
                        "   \"CHIPO\": 3," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser
    fun onlyAdminsCanDeleteBurgers() {
        val postResult = mockMvc.perform(
            post("/burgers").content(
                "{" +
                        "\"name\": \"Cannot be Deleted Burger\"," +
                        "\"ingredients\": {" +
                        "   \"REG_BUN\": 1," +
                        "   \"BAC\": 2," +
                        "   \"LETC\": 2," +
                        "   \"CHIPO\": 3," +
                        "   \"CH_PAT\": 1" +
                        " }" +
                        "}"
            ).contentType("application/json")
        ).andExpect(status().isCreated).andReturn()
        val newBurgerId = JsonPath.read<Integer>(postResult.response.contentAsString, "$.id")
        mockMvc.perform(delete("/burgers/$newBurgerId")).andExpect(status().isForbidden)
    }

}