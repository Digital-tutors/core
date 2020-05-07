package digital.tutors.autochecker.checker.services.impl

import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.repositories.UserRepository
import digital.tutors.autochecker.auth.services.impl.UserServiceImpl
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.repositories.TopicRepository
import digital.tutors.autochecker.checker.services.TopicService
import digital.tutors.autochecker.checker.vo.topic.TopicCreateRq
import digital.tutors.autochecker.checker.vo.topic.TopicUpdateRq
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TopicServiceImpl : TopicService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Autowired
    lateinit var topicRepository: TopicRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(EntityNotFoundException::class)
    override fun getTopicByIdOrThrow(id: String): TopicVO = topicRepository.findById(id).map(::toTopicVO).orElseThrow { throw EntityNotFoundException("Topic with $id not found.") }

    @Throws(EntityNotFoundException::class)
    override fun getPublicTopics(pageable: Pageable): Page<TopicVO> = topicRepository.findAllByAccessTypeEquals(AccessType.PUBLIC, pageable).map(::toTopicVO)

    override fun createTopic(topicCreateRq: TopicCreateRq): TopicVO {
        val id = topicRepository.save(Topic().apply {
            title = topicCreateRq.title
            accessType = topicCreateRq.accessType
            followers = topicCreateRq.followers?.map { User(id = it.id) }
            authorId = User(id = topicCreateRq.authorId?.id)
            contributors = topicCreateRq.contributors?.map { User(id = it.id) }
        }).id ?: throw IllegalArgumentException("Bad id returned.")

        log.debug("Created entity $id")
        return getTopicByIdOrThrow(id)
    }

    override fun subscribeTopic(id: String, userId: String) {
        val user = userRepository.findByIdOrNull(userId) ?: throw EntityNotFoundException("User with $id not found.")

        topicRepository.save(topicRepository.findById(id).get().apply {
            followers = followers?.plus(user)?.distinct()
        }).id

        log.debug("Updated followers entity $id")
    }

    @Throws(EntityNotFoundException::class)
    override fun updateTopic(id: String, topicUpdateRq: TopicUpdateRq): TopicVO {
        topicRepository.save(topicRepository.findById(id).get().apply {
            title = topicUpdateRq.title
            accessType = topicUpdateRq.accessType
            followers = topicUpdateRq.followers?.map { User(id = it.id) }
            contributors = topicUpdateRq.contributors?.map { User(id = it.id) }
        }).id

        log.debug("Updated entity $id")
        return getTopicByIdOrThrow(id)
    }

    @Throws(EntityNotFoundException::class)
    override fun delete(id: String) {
        topicRepository.deleteById(id)
        log.debug("Deleted advantage $id")
    }

    private fun toTopicVO(topic: Topic): TopicVO {
        return TopicVO.fromData(topic)
    }

}
