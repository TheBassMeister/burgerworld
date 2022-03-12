package com.bassmeister.burgerworld.repo

import com.bassmeister.burgerworld.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepo : CrudRepository<User, Long> {

    fun findUserByUserName(userName:String): Optional<User>
}