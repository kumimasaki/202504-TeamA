package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ec.com.models.entity.Users;
import ec.com.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserLoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private HttpSession session;

	// ログイン画面表示
	@GetMapping("/user/login")
	public String showLoginPage() {
		return "user_login.html";
	}

	// ログイン処理
	@PostMapping("/user/login/process")
	public String userLoginProcess(@RequestParam String email, @RequestParam String password) {

		// 入力されたメールアドレスとパスワードをチェック
		Users user = userService.loginCheck(email, password);

		// ユーザーが存在しない場合：エラー付きでログイン画面に戻る
		if (user == null) {
			return "redirect:/user/login?error";
		}

		// ログイン成功：セッションに保存し、講座一覧へリダイレクト
		session.setAttribute("loginUser", user);
		return "redirect:/user/menu";
	}

	// ログアウト処理
	@GetMapping("/lesson/menu/logout")
	public String UserLogout() {
		session.invalidate();
		return "redirect:/user/login";
	}
}
