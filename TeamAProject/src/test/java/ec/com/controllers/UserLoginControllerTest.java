package ec.com.controllers;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ec.com.models.entity.Users;
import ec.com.services.UserService;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService userService;
	private Users users;

	@BeforeEach
	public void prepareDate() {
		users = new Users();
		users.setUserName("Alice");
		users.setUserEmail("123@test.com");
		users.setUserPassword("1234abcd");
		when(userService.loginCheck("123@test.com", "1234abcd")).thenReturn(users);
		when(userService.loginCheck("test@test.com", "1234abcd")).thenReturn(null);
		when(userService.loginCheck("123@test.com", "1234")).thenReturn(null);
		when(userService.loginCheck("test@test.com", "1234")).thenReturn(null);
	}

	@Test
	public void testShowLoginPage_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/user/login");
		mockMvc.perform(request).andExpect(view().name("user_login.html"));
	}

	@Test
	public void testShowLoginPage_Fail() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/user/login").param("error", "");
		mockMvc.perform(request).andExpect(view().name("user_login.html"))
				.andExpect(model().attributeExists("errorMessage"));
	}

	@Test
	public void testUserLoginProcess_succeed() throws Exception {
		MockHttpSession session = new MockHttpSession();
		RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process").session(session)
				.param("userEmail", "123@test.com").param("userPassword", "1234abcd");
		MvcResult result = mockMvc.perform(request)
				.andExpect(redirectedUrl("/user/menu")).andReturn();
		HttpSession sessionAfterRequest = result.getRequest().getSession();
		Object usersInSession = sessionAfterRequest.getAttribute("users");
		verify(userService, times(1)).loginCheck("123@test.com", "1234abcd");
	}
	
	@Test
	public void testUserLoginProcess_InvalidEmail_False() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
				.param("userEmail", "test@test.com").param("userPassword", "1234abcd");
		mockMvc.perform(request).andExpect(redirectedUrl("/user/login?error"));
	}

	@Test
	public void testUserLoginProcess_InvalidPassword_False() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process").param("userEmail", "123@test.com")
				.param("userPassword", "1234");
		mockMvc.perform(request).andExpect(redirectedUrl("/user/login?error"));

	}

	@Test
	public void testUserLoginProcess_InvalidCredentials_False() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/login/process")
				.param("userEmail", "test@test.com").param("userPassword", "1234");
		mockMvc.perform(request).andExpect(redirectedUrl("/user/login?error"));
	}
	
	   @Test
	    public void testLogoutProcess() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.get("/user/logout"))
	                .andExpect(redirectedUrl("/user/menu"));
	    }
}
