package digital.tutors.autochecker.core.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
open class BaseCustomRepository {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    inline fun <reified T> findByUrl(url: String): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("translations.url").`is`(url))
        query.fields().include("_id")

        return mongoTemplate.find(query, T::class.java)
    }

    inline fun <reified T> findByPageIdAndLanguageAndLanguageAccess(pageId: String, languages: String): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("pageId").`is`(pageId).and("translations.language").`is`(languages).and("languageAccess").all(languages))
        query.fields().include("_id").include("translations.$").include("order")

        return mongoTemplate.find(query, T::class.java)
    }

    inline fun <reified T> findByLanguageAndLanguageAccess(languages: String, fields: List<String> = emptyList()): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("translations.language").`is`(languages).and("languageAccess").all(languages))
        query.fields().include("_id").include("translations.$").include("order")
        appendIncludes(query, fields)

        return mongoTemplate.find(query, T::class.java)
    }

    inline fun <reified T> findByLanguageAccess(languages: String, fields: List<String> = emptyList()): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("languageAccess").all(languages))
        query.fields().include("_id").include("order")
        appendIncludes(query, fields)

        return mongoTemplate.find(query, T::class.java)
    }

    inline fun <reified T> findByLanguageAndLanguageAccessAndContainsPageId(id: String, languages: String, fields: List<String> = emptyList()): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("translations.language").`is`(languages).and("languageAccess").all(languages).and("pages").all(id))
        query.fields().include("_id").include("translations.$").include("order")
        appendIncludes(query, fields)

        return mongoTemplate.find(query, T::class.java)
    }

    inline fun <reified T> findByLanguageAccessAndContainsPageId(id: String, languages: String, fields: List<String> = emptyList()): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("languageAccess").all(languages).and("pages").all(id))
        query.fields().include("_id").include("order")
        appendIncludes(query, fields)

        return mongoTemplate.find(query, T::class.java)
    }

    inline fun <reified T> findByPageIdAndLanguage(pageId: String, languages: String, fields: List<String> = emptyList()): List<T>? {
        val query = Query()
        query.addCriteria(Criteria.where("_id").`is`(pageId).and("translations.language").`is`(languages))
        query.fields().include("_id").include("translations.$").include("order")
        appendIncludes(query, fields)

        return mongoTemplate.find(query, T::class.java)
    }

    fun appendIncludes(query: Query, fields: List<String>) {
        for (field in fields) {
            query.fields().include(field)
        }
    }

}
