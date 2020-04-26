package digital.tutors.autochecker.checker.controllers

import digital.tutors.autochecker.checker.services.TaskService
import digital.tutors.autochecker.checker.vo.task.TaskVO
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TaskControllerTest.Config::class])
class TaskControllerTest {

    @MockBean
    private lateinit var taskService: TaskService

    private lateinit var task1: TaskVO;
    private lateinit var task2: TaskVO;



    @Configuration
    class Config
}