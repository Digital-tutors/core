package digital.tutors.autochecker.auth.controllers

import com.nhaarman.mockitokotlin2.*
import digital.tutors.autochecker.TestUtils
import digital.tutors.autochecker.auth.services.UserService
import digital.tutors.autochecker.auth.vo.UserCreateRq
import digital.tutors.autochecker.auth.vo.UserLoginRq
import digital.tutors.autochecker.auth.vo.UserVO
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
@SpringBootTest(classes = [UsersControllerTest.Config::class])
class UsersControllerTest {

    @MockBean
    private lateinit var userService: UserService

    private lateinit var users1: UserVO
    private lateinit var users2: UserVO

    private lateinit var mockMvcPublic: MockMvc

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        users1 = UserVO(
                id = "id1",
                email = "test1@test.com",
                token = null,
                firstName = "alex",
                lastName = "smith",
                confirmed = true
        )

        users2 = UserVO(
                id = "id2",
                email = "test2@test.com",
                token = null,
                firstName = "emma",
                lastName = "johnson",
                confirmed = true
        )
        whenever(userService.getUserByIdOrThrow(eq("id1"))).thenReturn(users1)
        whenever(userService.getUsers(any())).thenReturn(PageImpl(listOf(users1, users2)))

        whenever(userService.createUser(eq(UserCreateRq(
                "test1@test.com",
                "123",
                "alex",
                "smith"
        )))).thenReturn(users1)

        whenever(userService.loginUser(eq(UserLoginRq(
                "test1@test.com",
                "123"
        )))).thenReturn(users1)

        mockMvcPublic = MockMvcBuilders
                .standaloneSetup(UserController().apply {
                    userService = this@UsersControllerTest.userService
                })
                .build()
    }

    @Test
    fun findAllUsersPageable() {
        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.get("/users?page=0")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.[0].id").value("id1"))
                .andExpect(jsonPath("$.content.[1].id").value("id2"))
                .andExpect(jsonPath("$.content.[0].email").value("test1@test.com"))
                .andExpect(jsonPath("$.content.[1].email").value("test2@test.com"))
                .andExpect(jsonPath("$.content.[0].confirmed").value("true"))
                .andExpect(jsonPath("$.content.[1].confirmed").value("true"))
        verify(userService).getUsers(argThat {
            pageNumber == 0
        })
    }

    @Test
    fun findUsersById() {
        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.get("/user/id1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.email").value("test1@test.com"))
                .andExpect(jsonPath("$.confirmed").value("true"))
                .andExpect(jsonPath("$.firstName").value("alex"))
                .andExpect(jsonPath("$.lastName").value("smith"))
        verify(userService).getUserByIdOrThrow(eq("id1"))
    }

    @Test
    fun login() {
        val loginCreateRq = UserLoginRq(
                "test1@test.com",
                "123"
        )

        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.convertObjectToJsonBytes(loginCreateRq))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.email").value("test1@test.com"))
                .andExpect(jsonPath("$.confirmed").value("true"))
                .andExpect(jsonPath("$.firstName").value("alex"))
                .andExpect(jsonPath("$.lastName").value("smith"))
        verify(userService).loginUser(eq(loginCreateRq))
    }

    @Test
    fun signUp() {
        val signUpCreateRq = UserCreateRq(
                "test1@test.com",
                "123",
                "alex",
                "smith"
        )

        mockMvcPublic
                .perform(
                        MockMvcRequestBuilders.post("/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.convertObjectToJsonBytes(signUpCreateRq))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("id1"))
                .andExpect(jsonPath("$.email").value("test1@test.com"))
                .andExpect(jsonPath("$.confirmed").value("true"))
                .andExpect(jsonPath("$.firstName").value("alex"))
                .andExpect(jsonPath("$.lastName").value("smith"))
        verify(userService).createUser(eq(signUpCreateRq))
    }


    @Configuration
    class Config

}