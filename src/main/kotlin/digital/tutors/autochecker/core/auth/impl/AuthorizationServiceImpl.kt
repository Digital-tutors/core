package digital.tutors.autochecker.core.auth.impl

import digital.tutors.autochecker.core.auth.AuthorizationService
import digital.tutors.autochecker.core.auth.JWTDecoder
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthorizationServiceImpl : AuthorizationService {

    private val log = LoggerFactory.getLogger(AuthorizationServiceImpl::class.java)

    @Autowired
    lateinit var jwtDecoder: JWTDecoder

    override fun currentUserIdOrDie(): String =
            currentUserId()
                    .orElseThrow { EntityNotFoundException("No authentication found") }
                    .also { log.debug("Current user id: $it") }

    override fun currentUserId(): Optional<String> =
            Optional.ofNullable(
                    SecurityContextHolder
                            .getContext()
                            .authentication
                            ?.name
            )

    override fun getUserIdFromToken(token: String?): String? {
        if (token == null) {
            return null
        }
        return jwtDecoder.decode(token).subject
    }

}
