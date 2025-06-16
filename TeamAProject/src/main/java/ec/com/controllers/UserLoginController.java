package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String showLoginPage(@RequestParam(required = false) String error, Model model) {
		// error パラメータが存在する場合、エラーメッセージをモデルに追加
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが正しくありません。");
		}
		return "user_login.html";
	}

	// ログイン処理
	@PostMapping("/user/login/process")
	public String userLoginProcess(@RequestParam String userEmail, @RequestParam String userPassword) {

		// 入力されたメールアドレスとパスワードをチェック
		Users user = userService.loginCheck(userEmail, userPassword);

		// ユーザーが存在しない場合：エラー付きでログイン画面に戻る
		if (user == null) {
			// エラーパラメータをクエリ文字列に付加してリダイレクト
			return "redirect:/user/login?error";
		} else {
			// ログイン成功：セッションにユーザー情報を保存し、メニュー画面へリダイレクト
			session.setAttribute("loginUser", user);
			return "redirect:/user/menu";
		}
	}

	// ログアウト処理
	@GetMapping("/user/logout")
	public String userLogout() {
		session.invalidate();
		return "redirect:/user/menu";
	}
}
