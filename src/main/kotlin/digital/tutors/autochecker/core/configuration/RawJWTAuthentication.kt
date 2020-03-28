package digital.tutors.autochecker.core.configuration

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class RawJWTAuthentication(
        private val accessToken: String
) : Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    override fun getName(): String = "N/A"

    override fun getCredentials(): String = accessToken

    override fun getPrincipal(): Any? = null

    override fun isAuthenticated(): Boolean = false

    override fun getDetails(): Any? = null
}