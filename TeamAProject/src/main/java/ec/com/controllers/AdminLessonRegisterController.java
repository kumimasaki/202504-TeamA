package ec.com.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    // レッスン登録画面の表示
    @GetMapping("/admin/lesson/register")
    public String getLessonRegisterPage(Model model) {
    	// ログインチェック
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }
     // 管理者名をビューに渡す
        model.addAttribute("adminName", admin.getAdminName());
        return "admin_register_lesson";
    }

    // レッスン登録処理
    @PostMapping("/admin/lesson/register/create")
    public String lessonRegisterProcess(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime finishTime,
            @RequestParam String lessonDetail,
            @RequestParam String lessonName,
            @RequestParam Integer lessonFee,
            @RequestParam MultipartFile imageName) {
    	// ログインチェック
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }
     // 登録日時を現在時刻で取得
        LocalDateTime registerDate = LocalDateTime.now();
        boolean success = adminLessonService.createLesson(
                startDate, startTime, finishTime,
                lessonDetail, lessonName, lessonFee,
                imageName, registerDate, admin.getAdminId());
        // 登録成功なら確認画面へ、失敗ならフォームに戻る
        return success ? "admin_fix_register" : "admin_register_lesson";
    }
}
