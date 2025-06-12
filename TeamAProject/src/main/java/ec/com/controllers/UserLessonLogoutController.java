package ec.com.controllers;

import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

public class UserLessonLogoutController {
	@GetMapping("/lesson/menu/logout")
	public String menuLogout(HttpSession session) {
		// ログアウトの本体（サーバ側セッション削除）
	    session.invalidate();                
	    // 非ログイン版メニューへリダイレクト
	    return "redirect:/user/menu";  
	}
}
