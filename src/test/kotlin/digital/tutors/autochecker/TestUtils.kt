package digital.tutors.autochecker

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

class TestUtils {
    companion object {

        fun convertObjectToJsonBytes(obj: Any): ByteArray {
            val mapper = ObjectMapper()
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            val module = JavaTimeModule()
            mapper.registerModule(module)
            return mapper.writeValueAsBytes(obj)
        }

    }
}