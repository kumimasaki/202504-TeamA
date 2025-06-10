package ec.com.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import ec.com.models.entity.Admin;
import ec.com.services.AdminLessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonRegisterController {
	@Autowired
	private AdminLessonService adminLessonService;
	@Autowired
	private HttpSession session;
	@GetMapping("/admin/lesson/register")
	public String getBlogRegisterPage(Model model) {
		// セッションからログイン中のユーザーを取得
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		// 未ログインならログイン画面へリダイレクト
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			// ユーザー名を画面に渡す
			model.addAttribute("adminName", admin.getAdminName());
			return "admin_register_lesson";
		}

	}
	@PostMapping("/admin/lesson/register/create")
	public String lessonRegisterProcess(@RequestParam LocalDate startDate, @RequestParam LocalTime startTime,
			@RequestParam LocalTime finishTime, @RequestParam String lessonDetail,
			@RequestParam String lessonName,@RequestParam Integer lessonFee,@RequestParam MultipartFile imageName) {
		 // セッションからログインユーザーを取得
		Admin admin = (Admin) session.getAttribute("loginAdminInfo");
		 // 未ログインならログイン画面へ
		if (admin == null) {
			return "redirect:/admin/login";
		} else {
			// 画像ファイル名をタイムスタンプ付きで生成
			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
					+ imageName.getOriginalFilename();
			try {
				// アップロードされた画像を保存する
				Files.copy(imageName.getInputStream(), Path.of("./images/" + fileName));
			} catch (IOException e) {

				e.printStackTrace();
			}
			// 現在時刻を登録日時として設定
			LocalDateTime registerDate = LocalDateTime.now();
			// ブログをDBに登録
			if (adminLessonService.createLesson(startDate, startTime,finishTime,lessonDetail,lessonName,lessonFee,fileName,registerDate,admin.getAdminId())) {
				return "redirect:/admin/lesson/all";

			} else {
				return "admin_register_lesson";
			}
		}
	}
}