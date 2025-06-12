package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ec.com.services.UserService;

@Controller
@RequestMapping("/user")
public class UserPasswordResetController {

	@Autowired
	private UserService userService;

	@GetMapping("/password/reset")
	public String showResetPage() {
		return "user_password_reset.html";
	}

	// 入力されたメールアドレスの確認
	@PostMapping("/password/reset/mail")
	public String confirmMail(@RequestParam String userEmail, Model model) {

		// メールアドレス存在チェック
		if (userService.findByUserEmail(userEmail) == null) {
			// ユーザnull：?errorクエリを付けて入力画面へ戻す
			return "redirect:/user/password/reset?error";
		} else {
			// メール文字列をそのまま渡す
			model.addAttribute("userEmail", userEmail);
			return "user_password_change.html";
		}
	}

	// 新パスワードを保存してログイン画面へ
	@PostMapping("/change/password/complete")
	public String changePassword(@RequestParam String userEmail, @RequestParam String password,
			@RequestParam String passwordConfirm) {

		// 2回入力が一致チェック
		if (!password.equals(passwordConfirm)) {
			// 一致しない：mismatch を付けてredirect
			return "redirect:/user/password/reset?mismatch";
		} else {
			// 一致：パスワード更新後、ログイン画面へ
			userService.updatePassword(userEmail, password);
			return "redirect:/user/login";
		}
	}
}
