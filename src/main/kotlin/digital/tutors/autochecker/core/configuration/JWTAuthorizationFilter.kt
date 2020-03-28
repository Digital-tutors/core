package digital.tutors.autochecker.core.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.HEADER_STRING
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.SECRET
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.TOKEN_ISSUER
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.TOKEN_PREFIX
import io.jsonwebtoken.io.IOException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class JWTAuthorizationFilter(
        authManager: AuthenticationManager?
) : BasicAuthenticationFilter(authManager) {

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader(HEADER_STRING)
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        val authentication = getAuthentication(req)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(HEADER_STRING)
        if (token != null) {
            // parse the token.
            val decodedJWT = JWT.require(Algorithm.HMAC512(SECRET))
                    .withIssuer(TOKEN_ISSUER)
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
            val user = decodedJWT.payload
            val authority = getAuthorities(decodedJWT)

            return if (user != null) {
                UsernamePasswordAuthenticationToken(user, null, authority)
            } else null
        }
        return null
    }

    private fun getAuthorities(decodedJWT: DecodedJWT?): List<GrantedAuthority> =
            decodedJWT
                    ?.getClaim("role")
                    ?.asString()
                    ?.split(',')
                    ?.map { GrantedAuthority { it } }
                    ?.toList()
                    ?: emptyList()

}
