package digital.tutors.autochecker.checker.controllers

import com.nhaarman.mockitokotlin2.*
import digital.tutors.autochecker.TestUtils
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.AccessType
import digital.tutors.autochecker.checker.services.TopicService
import digital.tutors.autochecker.checker.vo.topic.TopicCreateRq
import digital.tutors.autochecker.checker.vo.topic.TopicUpdateRq
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import digital.tutors.autochecker.core.entity.EntityRefRq
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TopicControllerTest.Config::class])
class TopicControllerTest {

    @MockBean
    private lateinit var topicService: TopicService

    private lateinit var topic1: TopicVO
    private lateinit var topic2: TopicVO

    private lateinit var mockMvcPublic: MockMvc

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val user1 = UserVO(
                id = "id1",
                email = "test1@test.com",
                token = null,
                firstName = "emma",
                lastName = "johnson",
                confirmed = true
        )

        topic1 = TopicVO(
                "id1",
                "Title1",
                "PUBLIC",
                listOf(user1),
                user1,
                listOf(user1),
                "2015-08-02T00:29:53.949"
        )

        topic2 = TopicVO(
                "id2",
                "Title2",
                "PRIVATE",
                listOf(user1),
                user1,
                listOf(user1),
                "2015-08-02T00:29:53.949"
        )

        whenever(topicService.getTopicByIdOrThrow(eq("id1"))).thenReturn(topic1)
        whenever(topicService.getTopics(any())).thenReturn(PageImpl(listOf(topic1, topic2)))
        whenever(topicService.createTopic(any())).thenAnswer {
            topic1
        }
        whenever(topicService.updateTopic(any(), any())).thenAnswer {
            topic2.apply { id = "id1" }
        }
        whenever(topicService.delete(eq("nonexist"))).thenThrow(EntityNotFoundException("not found"))

        mockMvcPublic = MockMvcBuilders
                .standaloneSetup(TopicController().apply {
                    topicService = this@TopicControllerTest.topicService
                })
                .build()
    }

    @Test
    fun findAllTopicsPageable() {
        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.get("/topics?page=0")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].id").value("id1"))
                .andExpect(jsonPath("$.content.[1].id").value("id2"))
                .andExpect(jsonPath("$.content.[0].accessType").value("PUBLIC"))
                .andExpect(jsonPath("$.content.[1].accessType").value("PRIVATE"))
                .andExpect(jsonPath("$.content.[0].followers.[0].id").value("id1"))
                .andExpect(jsonPath("$.content.[0].createdDate").value("2015-08-02T00:29:53.949"))
        verify(topicService).getTopics(argThat {
            pageNumber == 0
        })
    }

    @Test
    fun findTopicById() {
        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.get("/topic/id1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.accessType").value("PUBLIC"))
                .andExpect(jsonPath("$.followers.[0].id").value("id1"))
                .andExpect(jsonPath("$.createdDate").value("2015-08-02T00:29:53.949"))
        verify(topicService).getTopicByIdOrThrow(eq("id1"))
    }

    @Test
    fun createTopic() {
        val topicCreateRq = TopicCreateRq(
                "Title1",
                AccessType.PUBLIC,
                listOf(EntityRefRq("id1")),
                EntityRefRq("id1"),
                listOf(EntityRefRq("id1"))
        )

        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.post("/topic")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.convertObjectToJsonBytes(topicCreateRq))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.accessType").value("PUBLIC"))
                .andExpect(jsonPath("$.followers.[0].id").value("id1"))
                .andExpect(jsonPath("$.createdDate").value("2015-08-02T00:29:53.949"))
        verify(topicService).createTopic(eq(topicCreateRq))
    }

    @Test
    fun updateTopic() {
        val topicUpdateRq = TopicUpdateRq(
                "Title2",
                AccessType.PRIVATE,
                listOf(EntityRefRq("id1")),
                listOf(EntityRefRq("id1"))
        )

        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.put("/topic/id1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.convertObjectToJsonBytes(topicUpdateRq))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.title").value("Title2"))
                .andExpect(jsonPath("$.accessType").value("PRIVATE"))
                .andExpect(jsonPath("$.followers.[0].id").value("id1"))
                .andExpect(jsonPath("$.createdDate").value("2015-08-02T00:29:53.949"))
        verify(topicService).updateTopic(eq("id1"), eq(topicUpdateRq))
    }

    @Test
    fun deleteTopicById() {
        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.delete("/topic/id1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
        verify(topicService).delete(eq("id1"))
    }

    @Test
    fun deleteTopicByIdNonexistent() {
        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.delete("/topic/nonexistent")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
        verify(topicService).delete(eq("nonexistent"))
    }

    @Configuration
    class Config

}