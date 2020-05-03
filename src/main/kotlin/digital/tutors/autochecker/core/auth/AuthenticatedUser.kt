package digital.tutors.autochecker.core.auth

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

fun Authentication.getUserOrNull(): AuthenticatedUser? {
    if (this is AnonymousAuthenticationToken) {
        return null
    }
    return principal as AuthenticatedUser
}

fun Authentication.getUser(): AuthenticatedUser {
    if (this is AnonymousAuthenticationToken)
        throw EntityNotFoundException("Anonymous user")
    return principal as AuthenticatedUser
}

fun currentAuthentication(): Authentication = SecurityContextHolder.getContext().authentication

fun currentUser(): User = currentAuthentication().getUser().user

fun currentUserId(): String = currentAuthentication().getUser().id

fun currentUserOrNull(): User? = currentAuthentication().getUserOrNull()?.user

fun currentUserIdOrNull(): String? = currentAuthentication().getUserOrNull()?.id

fun currentUserHasAnyRole(vararg roles: String) = SecurityContextHolder.getContext().authentication.authorities.any { roles.contains(it.authority) }

class AuthenticatedUser(
        val id: String,
        private val userRepository: UserRepository
) {
    val user: User by lazy {
        val user = userRepository.findByIdOrNull(id) ?: throw EntityNotFoundException("Anonymous user")
        user
    }
}
