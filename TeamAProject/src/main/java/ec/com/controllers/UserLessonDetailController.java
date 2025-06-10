package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;
import ec.com.models.entity.Users;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserLessonDetailController {
	@Autowired
	private HttpSession session;

	@Autowired
	private LessonDao lessonDao;

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
		Lesson lesson = lessonDao.findByLessonId(lessonId);
		model.addAttribute("lesson", lesson);
		return "user_lesson_detail.html";
	}
}
