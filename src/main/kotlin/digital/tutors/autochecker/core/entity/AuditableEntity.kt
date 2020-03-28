package digital.tutors.autochecker.core.entity

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

open class AuditableEntity {

    @CreatedBy
    var createdBy: String? = LocalDateTime.now().toString()

    @LastModifiedBy
    var modifiedBy: String? = null

    @CreatedDate
    var createdDt: LocalDateTime? = LocalDateTime.now()

    @LastModifiedDate
    var modifiedDt: LocalDateTime? = LocalDateTime.now()

}