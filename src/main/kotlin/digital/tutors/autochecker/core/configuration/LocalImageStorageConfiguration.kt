package digital.tutors.autochecker.core.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("vidint.ugc.local-image-storage")
data class LocalImageStorageConfiguration(
        val urlPrefix: String = "http://localhost/static/",
        val filesystemPath: String = "/var/vidint-ugc"
)