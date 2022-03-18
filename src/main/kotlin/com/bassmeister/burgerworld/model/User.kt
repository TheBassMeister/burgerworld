package com.bassmeister.burgerworld.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity(name="BurgerworldUser")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long,
    val userName: String,
    val pw: String,
    val enabled: Boolean,
    @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(name = "userAuthorities", joinColumns = [JoinColumn(name = "userId")])
    @Column(name = "authorities")
    val authorities: List<String>
) : UserDetails {

    override fun getAuthorities(): Collection<out GrantedAuthority> {
        return authorities.map { it -> SimpleGrantedAuthority(it) }.toList()
    }

    override fun getPassword(): String {
        return pw
    }

    override fun getUsername(): String {
        return userName
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    //Will be true as current version won't allow locked or expired users
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }


}