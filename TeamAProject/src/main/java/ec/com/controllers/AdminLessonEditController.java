package ec.com.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ec.com.models.entity.Admin;
import ec.com.models.entity.Lesson;
import ec.com.services.AdminLessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonEditController {

    @Autowired
    private AdminLessonService adminLessonService;

    @Autowired
    private HttpSession session;

 // レッスン編集ページを表示する
    @GetMapping("/admin/lesson/edit/{lessonId}")
    public String getLessonEditPage(@PathVariable Long lessonId, Model model) {
    	// ログインしている管理者情報をセッションから取得
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        // 指定されたレッスンIDに対応するレッスンを取得
        Lesson lesson = adminLessonService.lessonEditCheck(lessonId);
        if (lesson == null) {
            return "redirect:/admin/lesson/all";
        }
        // 管理者名とレッスン情報をモデルに追加して、編集ページへ
        model.addAttribute("adminName", admin.getAdminName());
        model.addAttribute("lesson", lesson);
        return "admin_edit_lesson";
    }

 // レッスン更新処理を実行する
    @PostMapping("/admin/lesson/edit/update")
    public String lessonUpdate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime finishTime,
            @RequestParam String lessonDetail,
            @RequestParam String lessonName,
            @RequestParam Integer lessonFee,
            @RequestParam(required = false) MultipartFile imageName,
            @RequestParam Long lessonId) {
    	 // ログイン中の管理者情報を取得
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }
     // 登録日時を現在時刻で更新
        LocalDateTime registerDate = LocalDateTime.now();
     // サービスを通じてレッスン情報を更新
        boolean success = adminLessonService.lessonUpdate(
                startDate, startTime, finishTime,
                lessonDetail, lessonName, lessonFee,
                imageName, registerDate, lessonId, admin.getAdminId());
     // 成功なら完了ページへ、失敗なら再度編集ページへリダイレクト
        return success ? "admin_fix_register" : "redirect:/admin/lesson/edit/" + lessonId;
    }
}