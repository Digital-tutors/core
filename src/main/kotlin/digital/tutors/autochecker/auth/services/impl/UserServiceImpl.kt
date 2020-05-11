package digital.tutors.autochecker.auth.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.UserService
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.core.services.impl.JwtServiceImpl
import digital.tutors.autochecker.auth.vo.UserCreateRq
import digital.tutors.autochecker.auth.vo.UserLoginRq
import digital.tutors.autochecker.auth.vo.UserVO
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        val userRepository: UserRepository,
        val jwtService: JwtServiceImpl,
        val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Throws(EntityNotFoundException::class)
    override fun getUserByIdOrThrow(id: String): UserVO = userRepository.findById(id).map(::toUserVO).orElseThrow { throw EntityNotFoundException("User with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getUsers(pageable: Pageable): Page<UserVO> = userRepository.findAll(pageable).map(::toUserVO)

    @Throws(EntityNotFoundException::class)
    override fun loginUser(user: UserLoginRq): UserVO {
        val foundUser = userRepository.findFirstByEmail(user.email)
        if (foundUser.password == null) throw BadCredentialsException("A password is empty")
        if (user.password == null) throw BadCredentialsException("A password is required")
        if (!bCryptPasswordEncoder.matches(user.password, foundUser.password)) throw BadCredentialsException("Passwords do not match")
        return toUserVOWithToken(foundUser, jwtService.getToken(foundUser))
    }

    override fun createUser(user: UserCreateRq): UserVO {
        val id = userRepository.save(User().apply {
            email = user.email
            password = bCryptPasswordEncoder.encode(user.password)
            firstName = user.firstName
            lastName = user.lastName
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getUserByIdOrThrow(id)
    }

    @Throws(EntityNotFoundException::class)
    override fun delete(id: String) {
        userRepository.deleteById(id)
        log.debug("Deleted page $id")
    }

    override fun existsByEmail(email: String): Boolean {
        if (userRepository.existsByEmail(email)) return true
        return false
    }

    private fun toUserVO(user: User): UserVO {
        return UserVO.fromData(user, null)
    }

    private fun toUserVOWithToken(user: User, token: String? = null): UserVO {
        return UserVO.fromData(user, token)
    }

}
