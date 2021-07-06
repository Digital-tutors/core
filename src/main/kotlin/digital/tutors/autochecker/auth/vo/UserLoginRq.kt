package digital.tutors.autochecker.auth.vo

import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserLoginRq(

        @get:NotNull
        @get:Email
        val email: String,

        @get:NotNull
        @get:Size(min = 8, max = 128)
        val password: String
)
