package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ec.com.models.entity.Users;
import ec.com.services.BookmarkService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/lesson/bookmark")
public class BookmarkController {

	@Autowired
	private BookmarkService bookmarkService;

	// HttpSession を利用してログインユーザ情報を取得するためのフィールド
	@Autowired
	private HttpSession session;

	@PostMapping("/toggle")
	public ResponseEntity<String> toggleBookmark(@RequestParam Long lessonId) {
		// HttpSession からログインしているユーザ情報を取得します。
		// ※ "loginUser" はログイン処理時にセッションに格納した属性名です。
		Users loginUser = (Users) session.getAttribute("loginUser");
		if (loginUser == null) {
			// ユーザ情報が見つからない場合は、未ログイン状態と判断し、401(Unauthorized) を返します。
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
		}
		// BookmarkService を呼び出し、ブックマークの切り替え処理を実行します。
		boolean added = bookmarkService.toggleBookmark(loginUser, lessonId);
		// 処理結果に応じて "added"（追加）または "removed"（解除）を返します。
		return ResponseEntity.ok(added ? "added" : "removed");
	}
}