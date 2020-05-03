package digital.tutors.autochecker.auth.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import digital.tutors.autochecker.core.controller.BaseController
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.auth.services.UserService
import digital.tutors.autochecker.auth.vo.UserCreateRq
import digital.tutors.autochecker.auth.vo.UserLoginRq
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.core.configuration.SecurityConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping
class UserController : BaseController() {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/api/login")
    fun login(@RequestBody userLoginRq: UserLoginRq): ResponseEntity<UserVO> = processServiceExceptions {
        ResponseEntity.ok(userService.loginUser(userLoginRq))
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody userCreateRq: UserCreateRq): ResponseEntity<UserVO> = processServiceExceptions {
        ResponseEntity.ok(userService.createUser(userCreateRq))
    }

    @PreAuthorize("hasRole(\"ADMIN\") or hasRole(\"SUPER_ADMIN\")")
    @GetMapping("/users")
    fun getUsers(@RequestParam page: Int): ResponseEntity<Page<UserVO>> = processServiceExceptions {
        try {
            val pageRequest = PageRequest.of(page, 10)
            ResponseEntity.ok(userService.getUsers(pageable = pageRequest))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Users Not Found", ex)
        }
    }

    @PreAuthorize("hasRole(\"ADMIN\") or hasRole(\"SUPER_ADMIN\")")
    @GetMapping("/user/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserVO> = processServiceExceptions {
        try {
            ResponseEntity.ok(userService.getUserByIdOrThrow(id = id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User Not Found", ex)
        }
    }

    @GetMapping("/user/me")
    fun getUserByToken(@RequestHeader(name="Authorization") token: String): ResponseEntity<UserVO> = processServiceExceptions {
        try {
            val decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET))
                    .withIssuer(SecurityConstants.TOKEN_ISSUER).build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
            val id = decodedJWT.getClaim("id").asString()

            ResponseEntity.ok(userService.getUserByIdOrThrow(id = id))
        } catch (ex: EntityNotFoundException) {
            throw ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User Not Found", ex)
        }
    }

}
