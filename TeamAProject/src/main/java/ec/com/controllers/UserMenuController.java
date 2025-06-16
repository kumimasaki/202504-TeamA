package ec.com.controllers;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;
import ec.com.models.entity.Users;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserMenuController {

	@Autowired
	private HttpSession session;

	@Autowired
	private LessonDao lessonDao;

	@GetMapping("/user/menu")
	public String showMenu(Model model,@RequestParam(required = false) String keyword) {
		// ログイン判定
		Users loginUser = (Users) session.getAttribute("loginUser");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);
		if (loginFlg) {
			model.addAttribute("userName", loginUser.getUserName());
		}

		// 講座一覧
		List<Lesson> lessonList;
		try {
			 if (keyword != null && !keyword.trim().isEmpty()) {
		            lessonList = lessonDao.findByLessonNameContainingIgnoreCase(keyword.trim());		            
		            model.addAttribute("keyword", keyword.trim());
		        } else {
		            lessonList = lessonDao.findAllByOrderByStartDateAscStartTimeAsc();
		        }
		} catch (Exception e) {
			lessonList = Collections.emptyList();
		}

		// 開催日が今日より前はメニューに表示しない
		LocalDate today = LocalDate.now();
		lessonList = lessonList.stream().filter(l -> l.getStartDate() != null && !l.getStartDate().isBefore(today))
				.collect(Collectors.toList());

		// 画面に渡す
		model.addAttribute("lessonList", lessonList);
		return "user_menu.html";
	}
}
