package digital.tutors.autochecker.auth.vo

import digital.tutors.autochecker.auth.entities.Roles
import digital.tutors.autochecker.auth.entities.User

data class UserVO(
        val id: String?,
        val email: String?,
        val token: String? = null,
        val firstName: String?,
        val lastName: String?,
        val confirmed: Boolean?,
        val role: Roles?
) {

    companion object {
        fun fromData(user: User?, token: String?): UserVO =
                UserVO(
                        user?.id,
                        user?.email,
                        token,
                        user?.firstName,
                        user?.lastName,
                        user?.confirmed,
                        user?.role
                )
    }

}
