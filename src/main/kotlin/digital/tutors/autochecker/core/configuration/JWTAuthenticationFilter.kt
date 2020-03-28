package digital.tutors.autochecker.core.configuration

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.EXPIRATION_TIME
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.HEADER_STRING
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.SECRET
import digital.tutors.autochecker.core.configuration.SecurityConstants.Companion.TOKEN_PREFIX
import io.jsonwebtoken.io.IOException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

open class JWTAuthenticationFilter(
        private val authenticationManagerBean: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication {
        val authorizationHeader = req.getHeader(HEADER_STRING)
        if (authorizationHeader == null || authorizationHeader.length < TOKEN_PREFIX.length) {
            throw AuthenticationServiceException("Missing header")
        }
        val token = authorizationHeader.substring(TOKEN_PREFIX.length)
        return authenticationManagerBean.authenticate(RawJWTAuthentication(token))
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest?,
                                          res: HttpServletResponse,
                                          chain: FilterChain,
                                          auth: Authentication) {
        val token = JWT.create()
                .withSubject((auth.principal as User).username)
                .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET))
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token)
    }

    override fun unsuccessfulAuthentication(req: HttpServletRequest?, res: HttpServletResponse?, failed: AuthenticationException?) {
        SecurityContextHolder.clearContext()
        failureHandler.onAuthenticationFailure(req, res, failed)
    }

}