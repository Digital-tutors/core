package digital.tutors.autochecker.core.services.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.core.configuration.SecurityConstants
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtServiceImpl {

    @Value("\${jwt.signing-key}")
    lateinit var signingKey: String

    private fun createAccessToken(user: User): String {
        val issuedAt = Date()
        val calendar = Calendar.getInstance()
        calendar.time = issuedAt
        calendar.add(Calendar.MILLISECOND, SecurityConstants.EXPIRATION_TIME)

        return JWT.create()
                .withSubject(user.id)
                .withClaim("id", user.id)
                .withClaim("firstName", user.firstName)
                .withClaim("lastName", user.lastName)
                .withClaim("scopes", user.role.toString())
                .withIssuer(SecurityConstants.TOKEN_ISSUER)
                .withIssuedAt(issuedAt)
                .withExpiresAt(calendar.time)
                .sign(Algorithm.HMAC512(signingKey))
    }

    fun getToken(user: User): String = createAccessToken(user)

}
