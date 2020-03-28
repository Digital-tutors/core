package digital.tutors.autochecker.core.controller

import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.server.ResponseStatusException

open class BaseController {

    private val log = LoggerFactory.getLogger(BaseController::class.java)

    protected fun <T> processServiceExceptions(block: () -> T) =
            try {
                block()
            } catch (e: BadCredentialsException) {
                log.error("$e")
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Bad credentials", e)
            } catch (e: EntityNotFoundException) {
                log.error("$e")
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found", e)
            } catch (e: Exception) {
                log.error("$e")
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred processing the request", e)
            }

}