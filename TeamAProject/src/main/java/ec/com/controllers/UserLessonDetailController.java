package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.dao.CommentDao;
import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Comment;
import ec.com.models.entity.Lesson;
import ec.com.models.entity.Users;
import ec.com.services.CommentService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserLessonDetailController {
	@Autowired
	private HttpSession session;

	@Autowired
	private LessonDao lessonDao;

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentDao commentDao;

	// 講座詳細画面を表示
	@GetMapping("/lesson/detail/{lessonId}")
	public String lessonDetail(@PathVariable Long lessonId, Model model) {
		// 登録判定
		Users loginUser = (Users) session.getAttribute("loginUser");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);
		if (loginFlg) {
			model.addAttribute("userName", loginUser.getUserName());
		}
		// 講座情報取得
		Lesson lesson = lessonDao.findByLessonId(lessonId);
		model.addAttribute("lesson", lesson);
		// コメント一覧取得
		List<Comment> comments = commentService.getCommentsByLessonId(lessonId);
		model.addAttribute("comments", comments);
		return "user_lesson_detail.html";
	}

	// コメント投稿処理
	@PostMapping("/lesson/comment/add")
	public String addComment(@RequestParam("lessonId") Long lessonId, @RequestParam("content") String content) {
		// ログインチェック
		Users loginUser = (Users) session.getAttribute("loginUser");

		if (loginUser == null) {
			// ログインしていない場合
			return "redirect:/user/login";
		}

		Lesson lesson = lessonDao.findByLessonId(lessonId);
		if (lesson != null && content != null && !content.trim().isEmpty()) {
			commentService.addComment(lesson, loginUser, content);
		}

		// 投稿後に詳細画面へリダイレクト
		return "redirect:/lesson/detail/" + lessonId;
	}

	@PostMapping("/lesson/comment/delete")
	public String deleteComment(@RequestParam Long commentId, @RequestParam Long lessonId) {
		Users loginUser = (Users) session.getAttribute("loginUser");

		// ログインしていない場合
		if (loginUser == null) {
			return "redirect:/user/login";
		}

		Comment comment = commentDao.findById(commentId).orElse(null);

		// コメントが存在し、かつ投稿者が現在のユーザーと一致する場合のみ削除
		if (comment != null && comment.getUser().getUserId().equals(loginUser.getUserId())) {
			commentDao.delete(comment);
		}

		// 講座詳細ページにリダイレクト
		return "redirect:/lesson/detail/" + lessonId;
	}

}
