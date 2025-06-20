package ec.com.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ec.com.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegisterControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService userService;

	@BeforeEach
	public void prepareDate() {
		when(userService.createUser(eq("Taro"), any(), any())).thenReturn(false);
		when(userService.createUser("Alice", "123@test.com", "1234abcd")).thenReturn(true);
	}

	@Test
	public void testGetAdminRegisterPage_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/user/register");
		mockMvc.perform(request).andExpect(view().name("user_register.html"));
	}

	@Test
	public void testConfirmUserRegister() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.get("/user/confirm");
		mockMvc.perform(request).andExpect(view().name("user_confirm_register.html"));
	}

	@Test
	public void testUserRegisterProcess_NewAccount_Succeed() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process").param("userName", "Alice")
				.param("userEmail", "123@test.com").param("password", "1234abcd").param("userPassword", "1234abcd");
		mockMvc.perform(request).andExpect(view().name("user_confirm_register.html"));
	}

	@Test
	public void testUserRegisterProcess_ExistingUsername_fail() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process").param("userName", "Taro")
				.param("userEmail", "123@test.com").param("password", "123abcd").param("userPassword", "1234abcd");
		mockMvc.perform(request).andExpect(view().name("user_register.html"));
	}

	@Test
	public void testUserRegisterProcess_ExistingUserPassword_fail() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/register/process").param("userName", "Alice")
				.param("userEmail", "123@test.com").param("password", "123abc").param("userPassword", "1234abcd");
		mockMvc.perform(request).andExpect(view().name("user_register.html"));
	}

	@Test
	public void testConfirmProcess_Success() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/confirm/process").param("userName", "Alice")
				.param("userEmail", "123@test.com").param("userPassword", "1234abcd");
		mockMvc.perform(request).andExpect(view().name("user_login.html"));
		verify(userService, times(1)).createUser("Alice", "123@test.com", "1234abcd");
	}

	@Test
	public void testConfirmProcess_Fail() throws Exception {
		RequestBuilder request = MockMvcRequestBuilders.post("/user/confirm/process").param("userName", "Taro")
				.param("userEmail", "123@test.com").param("userPassword", "1234abcd");
		mockMvc.perform(request).andExpect(view().name("user_register.html"));
	}
}
