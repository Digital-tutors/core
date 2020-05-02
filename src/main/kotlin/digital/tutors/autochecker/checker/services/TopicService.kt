package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.core.exception.EntityNotFoundException
import digital.tutors.autochecker.checker.vo.topic.TopicCreateRq
import digital.tutors.autochecker.checker.vo.topic.TopicUpdateRq
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TopicService {

    @Throws(EntityNotFoundException::class)
    fun getTopicByIdOrThrow(id: String): TopicVO

    @Throws(EntityNotFoundException::class)
    fun getPublicTopics(pageable: Pageable): Page<TopicVO>

    fun createTopic(topicCreateRq: TopicCreateRq): TopicVO

    @Throws(EntityNotFoundException::class)
    fun updateTopic(id: String, topicUpdateRq: TopicUpdateRq): TopicVO

    @Throws(EntityNotFoundException::class)
    fun delete(id: String)

}
