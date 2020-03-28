package digital.tutors.autochecker.checker.services

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import digital.tutors.autochecker.auth.entities.User
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.entities.Topic
import digital.tutors.autochecker.checker.repositories.TopicRepository
import digital.tutors.autochecker.checker.services.impl.TopicServiceImpl
import digital.tutors.autochecker.checker.vo.topic.TopicCreateRq
import digital.tutors.autochecker.checker.vo.topic.TopicUpdateRq
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import digital.tutors.autochecker.core.entity.EntityRefRq
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime
import java.util.*

class TopicServiceImplTest {

    @InjectMocks
    lateinit var topicServiceImpl: TopicServiceImpl

    @Mock
    lateinit var topicRepository: TopicRepository

    private lateinit var topic1: Topic
    private lateinit var topicVO1: TopicVO

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val date = LocalDateTime.now().toString()

        val user1 = User(
                id = "id1",
                email = "test1@test.com",
                firstName = "emma",
                lastName = "johnson",
                confirmed = true
        )

        topic1 = Topic(
                "id1",
                "Title1",
                AccessType.PUBLIC,
                listOf(user1),
                user1,
                listOf(user1)
        ).apply { createdBy = date }

        val userVO1 = UserVO(
                id = "id1",
                email = "test1@test.com",
                token = null,
                firstName = "emma",
                lastName = "johnson",
                confirmed = true
        )

        topicVO1 = TopicVO(
                "id1",
                "Title1",
                "PUBLIC",
                listOf(userVO1),
                userVO1,
                listOf(userVO1),
                date
        )

        Mockito.`when`(topicRepository.save(any<Topic>())).thenReturn(topic1)
        Mockito.`when`(topicRepository.findById("id1")).thenReturn(Optional.of(topic1))
    }

    @Test
    fun update() {
        val topicResult = topicServiceImpl.updateTopic(
                "id1",
                TopicUpdateRq(
                        "Title1",
                        AccessType.PUBLIC,
                        listOf(EntityRefRq("id1")),
                        listOf(EntityRefRq("id1"))
                )
        )
        assertEquals(topicVO1.id, topicResult.id)
        assertEquals(topicVO1.accessType, topicResult.accessType)
        assertEquals(topicVO1.title, topicResult.title)
        assertEquals(topicVO1.followers?.first()?.id, topicResult.followers?.first()?.id)
        assertEquals(topicVO1.createdDate, topicResult.createdDate)
    }

    @Test
    fun create() {
        val topicResult = topicServiceImpl.createTopic(
                TopicCreateRq(
                        "Title1",
                        AccessType.PUBLIC,
                        listOf(EntityRefRq("id1")),
                        EntityRefRq("id1"),
                        listOf(EntityRefRq("id1"))
                )
        )
        assertEquals(
                topicVO1,
                topicResult
        )
    }

    @Test
    fun delete() {
        assertEquals(
                topicServiceImpl.delete(eq("id1")),
                topicRepository.deleteById("id1")
        )
    }
}