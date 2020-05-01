package digital.tutors.autochecker.checker.controllers

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import digital.tutors.autochecker.TestUtils
import digital.tutors.autochecker.auth.vo.UserVO
import digital.tutors.autochecker.checker.entities.Level
import digital.tutors.autochecker.checker.entities.Options
import digital.tutors.autochecker.checker.entities.Test
import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskUpdateRq
import digital.tutors.autochecker.checker.vo.task.TaskVO
import digital.tutors.autochecker.checker.vo.topic.TopicVO
import digital.tutors.autochecker.core.entity.EntityRefRq
import digital.tutors.autochecker.core.exception.EntityNotFoundException
import org.junit.Before
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TaskControllerTest.Config::class])
class TaskControllerTest {

    @MockBean
    private lateinit var taskService: TaskService

    @MockBean
    private lateinit var taskController: TaskController

    private lateinit var taskVO1: TaskVO
    private lateinit var taskVO2: TaskVO
    private lateinit var task2: TaskVO
    private lateinit var options1: Options
    private lateinit var test1: Test
    private lateinit var topic1: TopicVO
    private lateinit var author1: UserVO
    private lateinit var user1: UserVO
    private lateinit var user2: UserVO
    private lateinit var mockMvcPublic: MockMvc

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        //add one lecturer
         author1 = UserVO(
                id = "1",
                email = "test@gmail.com",
                token = null,
                firstName = "Prepod1",
                lastName = "MacLovin",
                confirmed = null
        )

        //add some students
         user1 = UserVO(
                id = "2",
                email = "test@mail.com",
                token = null,
                firstName = "Student1",
                lastName = "MacLovin",
                confirmed = null
        )
         user2 = UserVO(
                id = "3",
                email = "test@mail.com",
                token = null,
                firstName = "Student2",
                lastName = "MacLovin",
                confirmed = null
        )

         topic1 = TopicVO(
                id ="1",
                title = "titleTest",
                accessType = "PUBLIC",
                followers = listOf(user1, user2),
                authorId = author1,
                contributors = listOf(author1),
                createdDate = "2012-06-02T22:29:53.949"
        )

         options1 = Options (
                constructions = listOf("if", "for"),
                timeLimit = "5m",
                memoryLimit = "50KB"
        )
        test1 = Test (
                input = listOf("1", "2", "3"),
                output = listOf("6", "5", "4")
        )

        taskVO1 = TaskVO(
                id = "1",
                topicId = topic1,
                authorId = author1,
                description = "descrioptionTest",
                contributors = listOf(user1, user2),
                options = options1,
                test = test1,
                level = Level.MIDDLE
                )

        taskVO2 = TaskVO(
                id = "2",
                topicId = topic1,
                authorId = author1,
                description = "descrioptionTest",
                contributors = listOf(user1, user2),
                options = options1,
                test = test1,
                level = Level.SENIOR
        )

        //get task by actually id
        whenever(taskService.getTaskByIdOrThrow(eq("1"))).thenReturn(taskVO1)
        //get all tasks
        whenever(taskService.getTasks(any())).thenReturn(PageImpl(listOf(taskVO1, taskVO2)))
        //create task1
        whenever(taskService.createTask(any())).thenAnswer {
            taskVO1
        }
        //update
        whenever(taskService.updateTask(any(), any())).thenAnswer {
            taskVO1.apply { id = "2" }
        }
        //delete
        whenever(taskService.delete(eq("nonexist"))).thenThrow(EntityNotFoundException("not found"))

        mockMvcPublic = MockMvcBuilders.standaloneSetup(TaskController().apply {
                    taskService = this@TaskControllerTest.taskService
                }).build()
    }

    @org.junit.Test
    fun findTaskById() {
        mockMvcPublic.perform(
                MockMvcRequestBuilders.get("/task/1").contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @org.junit.Test
    fun findAllTask() {
        mockMvcPublic.perform(
                MockMvcRequestBuilders.get("/tasks?page=0").contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @org.junit.Test
    fun deleteTaskById() {
        mockMvcPublic.perform(MockMvcRequestBuilders.delete("/task/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @org.junit.Test
    fun updateTask() {
        val taskUpdateRq = TaskUpdateRq(
                topicId = EntityRefRq("1"),
                contributors = listOf(
                        EntityRefRq("1"),
                        EntityRefRq("2")),
                level = Level.JUNIOR,
                description = "OtherDescription",
                test = test1,
                options = options1
        )

        mockMvcPublic.perform(
                MockMvcRequestBuilders.put("/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectToJsonBytes(taskUpdateRq))
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
    }



    @Configuration
    class Config
}