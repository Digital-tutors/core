package digital.tutors.autochecker.auth.vo


import javax.validation.constraints.*

data class UserCreateRq(

        @get:NotNull
        @get:NotEmpty
        @get:Email
        val email: String,


        @get:NotNull
        @get:Size(min = 8, max = 128)
        val password: String,

        val firstName: String?,
        val lastName: String?
)
