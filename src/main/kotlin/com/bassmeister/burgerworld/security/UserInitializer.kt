package com.bassmeister.burgerworld.security

import com.bassmeister.burgerworld.model.User
import com.bassmeister.burgerworld.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@Profile("dev")
class UserInitializer {

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
}