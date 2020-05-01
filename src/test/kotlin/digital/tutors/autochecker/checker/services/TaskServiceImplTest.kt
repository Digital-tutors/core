package digital.tutors.autochecker.checker.services

import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.*
import digital.tutors.autochecker.checker.repositories.TaskRepository
import digital.tutors.autochecker.checker.services.impl.TaskServiceImpl
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TaskServiceImplTest {
    @InjectMocks
    lateinit var taskServiceImpl: TaskServiceImpl

    @Mock
    lateinit var taskRepository: TaskRepository

    private lateinit var taskVO1: TaskVO
    private lateinit var topicVO1: TopicVO
    private lateinit var author1: UserVO


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        author1 = UserVO (
                id = "1",
                email = "test@gmail.com",
                token = null,
                firstName = "Prepod1",
                lastName = "MacLovin",
                confirmed = null
        )

        val options1 = Options (
                constructions = listOf("if", "for"),
                timeLimit = "5m",
                memoryLimit = "50KB"
        )

        val test1 = Test (
                input = listOf("1", "2", "3"),
                output = listOf("6", "5", "4")
        )

        //add some students
        val user1 = UserVO(
                id = "2",
                email = "test@mail.com",
                token = null,
                firstName = "Student1",
                lastName = "MacLovin",
                confirmed = null
        )
        val user2 = UserVO(
                id = "3",
                email = "test@mail.com",
                token = null,
                firstName = "Student2",
                lastName = "MacLovin",
                confirmed = null
        )

        topicVO1 = TopicVO(
                id ="1",
                title = "titleTest",
                accessType = "PUBLIC",
                followers = listOf(user1, user2),
                authorId = author1,
                contributors = listOf(author1),
                createdDate = "2012-06-02T22:29:53.949"
        )

        taskVO1 = TaskVO(
                id = "1",
                topicId = topicVO1,
                authorId = author1,
                description = "descrioptionTest",
                contributors = listOf(user1, user2),
                options = options1,
                test = test1,
                level = Level.SENIOR
        )

        Mockito.`when`(taskServiceImpl.getTaskByTopicId(topicVO1.id.toString())).thenReturn(listOf(taskVO1))
        Mockito.`when`(taskServiceImpl.getTasksByAuthorId(author1.id.toString())).thenReturn(listOf(taskVO1))

    }

    @org.junit.Test
    fun findTaskByTopicId() {
        val resultGetTaskByTopic = taskServiceImpl.getTaskByTopicId(topicVO1.id.toString())
        assertEquals(taskVO1.authorId, resultGetTaskByTopic[0].authorId)
        assertEquals(taskVO1.topicId, resultGetTaskByTopic[0].topicId)
        assertEquals(taskVO1.description, resultGetTaskByTopic[0].description)
        assertEquals(taskVO1.contributors, resultGetTaskByTopic[0].contributors)
        assertEquals(taskVO1.options, resultGetTaskByTopic[0].options)
        assertEquals(taskVO1.test, resultGetTaskByTopic[0].test)
        assertEquals(taskVO1.level, resultGetTaskByTopic[0].level)
    }

    @org.junit.Test
    fun findTaskByAuthorId() {
        val resultGetTaskByAuthorId = taskServiceImpl.getTasksByAuthorId(author1.id.toString())
        assertEquals(taskVO1.authorId, resultGetTaskByAuthorId[0].authorId)
        assertEquals(taskVO1.topicId, resultGetTaskByAuthorId[0].topicId)
        assertEquals(taskVO1.description, resultGetTaskByAuthorId[0].description)
        assertEquals(taskVO1.contributors, resultGetTaskByAuthorId[0].contributors)
        assertEquals(taskVO1.options, resultGetTaskByAuthorId[0].options)
        assertEquals(taskVO1.test, resultGetTaskByAuthorId[0].test)
        assertEquals(taskVO1.level, resultGetTaskByAuthorId[0].level)
    }


}