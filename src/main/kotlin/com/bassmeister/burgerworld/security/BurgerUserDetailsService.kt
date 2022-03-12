package com.bassmeister.burgerworld.security

import com.bassmeister.burgerworld.repo.UserRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class BurgerUserDetailsService(@Autowired val userRepo: UserRepo) : UserDetailsService {

    override fun loadUserByUsername(userName: String): UserDetails {
        val user=userRepo.findUserByUserName(userName)
        if(!user.isPresent){
            throw UsernameNotFoundException("User $userName is unknown")
        }
        return user.get()
    }


}