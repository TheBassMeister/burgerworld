package com.bassmeister.burgerworld.controller

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.repo.BurgerRepo
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/burgers", produces = [APPLICATION_JSON_VALUE])
class BurgerController(@Autowired val burgerRepo: BurgerRepo) {

    @GetMapping
    fun getAllBurgers(@RequestParam(required = false) isCustom: Boolean?): List<Burger> {
        return if (isCustom != null) {
            burgerRepo.findAll().filter { it.isCustom == isCustom }.toList()
        } else {
            burgerRepo.findAll().toList()
        }
    }

    @GetMapping("/{name}")
    fun getBurgerByName(@PathVariable name: String): ResponseEntity<Burger> {
        val burger = burgerRepo.getBurgerByName(name)
        return if (burger.isPresent) {
            ResponseEntity(burger.get(), HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"]) @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    fun createNewBurger(@Valid @RequestBody burger: Burger, bindingResult: BindingResult): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bindingResult.allErrors[0].defaultMessage)
        }
        if (burgerRepo.getBurgerByName(burger.name).isPresent) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Burger with name ${burger.name} already exists")
        }
        burger.isCustom=true
        val newBurger = burgerRepo.save(burger)
        return ResponseEntity(newBurger, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteBurger(@PathVariable id: Long): ResponseEntity<String> {
        val burger = burgerRepo.findById(id)
        return if (burger.isEmpty) {
            ResponseEntity(HttpStatus.OK)
        } else if (!burger.get().isCustom) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only custom Burgers can be deleted")
        } else {
            burgerRepo.delete(burger.get())
            ResponseEntity(HttpStatus.OK)
        }
    }

}