package digital.tutors.autochecker.core.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import digital.tutors.autochecker.core.configuration.SecurityConstants
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component

@Component
class JWTDecoder {

    @Value("\${jwt.signing-key}")
    lateinit var signingKey: String

    fun sign(jwtBuilder: JWTCreator.Builder): String =
            jwtBuilder.sign(Algorithm.HMAC512(signingKey.toByteArray()))

    fun decode(token: String): DecodedJWT =
            JWT
                    .require(Algorithm.HMAC512(signingKey.toByteArray()))
                    .build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))

    fun getAuthorities(decodedJWT: DecodedJWT?): List<GrantedAuthority> =
            decodedJWT
                    ?.getClaim("scopes")
                    ?.asString()
                    ?.split(',')
                    ?.map { GrantedAuthority { it } }
                    ?.toList()
                    ?: emptyList()

}
