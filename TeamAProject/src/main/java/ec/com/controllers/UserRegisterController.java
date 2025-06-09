package ec.com.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.services.UserService;


@Controller
public class UserRegisterController {
	@Autowired
	private UserService userService;
	//登録画面を表示
	@GetMapping("/user/register")
	public String getUserRegisterPage() {
		return "user_register.html";
	}
	//登録確認画面を表示
	@GetMapping("/user/confirm")
	public String confirmUserRegister() {
		return "user_confirm_register.html";
	}
	// 登録処理
	@PostMapping("/user/register/process")
	public String userRegisterProcess(@RequestParam String userName, @RequestParam String userEmail,
			@RequestParam String password, @RequestParam String userPassword, Model model) {
		//ダブルチェック
		if (!password.equals(userPassword)) {
			return "user_register.html";
		} else {
			model.addAttribute("userName", userName);
			model.addAttribute("userEmail", userEmail);
			model.addAttribute("password", password);
			return "user_confirm_register.html";
		}

	}

	@PostMapping("/user/confirm/process")
	public String createUser(@RequestParam String userName, @RequestParam String userEmail,
			@RequestParam String password) {
		if (userService.createUser(userName, userEmail, password)) {
			return "user_login.html";
		} else {
			return "user_register.html";
		}

	}
}
