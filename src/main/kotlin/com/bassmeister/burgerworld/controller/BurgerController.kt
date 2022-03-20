package com.bassmeister.burgerworld.controller

import com.bassmeister.burgerworld.model.Burger
import com.bassmeister.burgerworld.repo.BurgerRepo
import mu.KotlinLogging
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

    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getAllBurgers(@RequestParam(required = false) isCustom: Boolean?): List<Burger> {
        return if (isCustom != null) {
            logger.info { "Got a Request for all Burgers with isCustom flag set to $isCustom" }
            burgerRepo.findAll().filter { it.isCustom == isCustom }.toList()
        } else {
            logger.info { "Requested All Burgers" }
            burgerRepo.findAll().toList()
        }
    }

    @GetMapping("/{name}")
    fun getBurgerByName(@PathVariable name: String): ResponseEntity<Burger> {
        logger.info { "Got a Request for find Burger with name $name" }
        val burger = burgerRepo.getBurgerByName(name)
        return if (burger.isPresent) {
            logger.info { "Burger with name '$name' found" }
            ResponseEntity(burger.get(), HttpStatus.OK)
        } else {
            logger.info { "Burger with name '$name' not found" }
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"]) @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    fun createNewBurger(@Valid @RequestBody burger: Burger, bindingResult: BindingResult): ResponseEntity<Any> {
        logger.info { "Got a request to create a new burger" }
        if (bindingResult.hasErrors()) {
            val errorMsg=bindingResult.allErrors[0].defaultMessage
            logger.error { "User requested burger with invalid input $errorMsg" }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg)
        }
        if (burgerRepo.getBurgerByName(burger.name).isPresent) {
            logger.error { "Burger with name ${burger.name} already exists" }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Burger with name ${burger.name} already exists")
        }
        burger.isCustom=true
        val newBurger = burgerRepo.save(burger)
        logger.info { "Burger with name ${newBurger.name} successfully created" }
        return ResponseEntity(newBurger, HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteBurger(@PathVariable id: Long): ResponseEntity<String> {
        logger.info { "Got a Request to delete burger with id $id" }
        val burger = burgerRepo.findById(id)
        return if (burger.isEmpty) {
            logger.info { "Burger with given id does not exist. Nothing got deleted" }
            ResponseEntity(HttpStatus.OK)
        } else if (!burger.get().isCustom) {
            logger.error { "Only custom burgers can be deleted" }
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only custom Burgers can be deleted")
        } else {
            burgerRepo.delete(burger.get())
            logger.info { "Burger successfully deleted" }
            ResponseEntity(HttpStatus.OK)
        }
    }

}