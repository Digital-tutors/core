package digital.tutors.autochecker.core.utils

import digital.tutors.autochecker.core.configuration.LocalImageStorageConfiguration
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Component
class FilesUpload {

    private val log = LoggerFactory.getLogger(FilesUpload::class.java)

    fun handleFileUpload(file: MultipartFile): String {
        log.info("handling fileupload for {}", file.name)
        return uploadImage(file)
    }

    private fun uploadImage(image: MultipartFile): String {
        val findExtension = if (image.contentType !== null)
            image.contentType!!.split("/")[1]
        else
            "png"

        val name = UUID.randomUUID().toString().replace("-", "") + ".${findExtension}"
        Files.copy(image.inputStream, Paths.get(LocalImageStorageConfiguration().filesystemPath, name))
        return LocalImageStorageConfiguration().urlPrefix + name
    }

}