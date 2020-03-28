package digital.tutors.autochecker.core.services.impl

import digital.tutors.autochecker.auth.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import digital.tutors.autochecker.auth.entities.User as UserEntity

@Service
class UserDetailsServiceImpl(
        val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        // TODO: Корректно ли такое делать?
        val user: UserEntity = userRepository.findByEmail(username)
        return User(user.email, user.password, listOf(SimpleGrantedAuthority(user.role.toString())))
    }

}