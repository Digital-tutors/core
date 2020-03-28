package digital.tutors.autochecker.auth.vo

data class UserCreateRq(
        val email: String,
        val password: String,
        val firstName: String?,
        val lastName: String?
)