package digital.tutors.autochecker.auth.repositories

import digital.tutors.autochecker.auth.entities.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User
    fun findFirstByEmail(email: String): User
}