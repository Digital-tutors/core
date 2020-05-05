package digital.tutors.autochecker.core.configuration

class SecurityConstants {

    companion object {

        const val HEADER_STRING = "Authorization"
        const val TOKEN_PREFIX = "Bearer "
        const val EXPIRATION_TIME = 864_000_000
        const val TOKEN_ISSUER = "checker.localhost"

    }

}
