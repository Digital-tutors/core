package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.vo.TopicVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TopicService {

    @Throws(EntityNotFoundException::class)
    fun getTopicByIdOrThrow(id: String): TopicVO

    @Throws(EntityNotFoundException::class)
    fun getTopics(pageable: Pageable): Page<TopicVO>

    fun createTopic(topic: Topic): TopicVO

//    @Throws(EntityNotFoundException::class)
//    fun updateUser(id: String, page: PageUpdateRq): PageVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)

}