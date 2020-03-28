package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.repositories.TopicRepository
import digital.tutors.autochecker.checker.services.TopicService
import digital.tutors.autochecker.checker.vo.TopicVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class TopicServiceImpl : TopicService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var topicRepository: TopicRepository

    @Throws(EntityNotFoundException::class)
    override fun getTopicByIdOrThrow(id: String): TopicVO = topicRepository.findById(id).map(::toTopicVO).orElseThrow { throw EntityNotFoundException("Topic with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getTopics(pageable: Pageable): Page<TopicVO> = topicRepository.findAll(pageable).map(::toTopicVO)

    override fun createTopic(topic: Topic): TopicVO {
        val id = topicRepository.save(topic).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getTopicByIdOrThrow(id)
    }

    override fun delete(id: String) {
        TODO("Not yet implemented")
    }

    private fun toTopicVO(topic: Topic): TopicVO {
        return TopicVO.fromData(topic)
    }

}