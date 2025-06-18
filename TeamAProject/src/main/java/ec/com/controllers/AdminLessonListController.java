package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



import ec.com.models.entity.Admin;
import ec.com.models.entity.Lesson;
import ec.com.services.AdminLessonService;
import jakarta.servlet.http.HttpSession;



@Controller
public class AdminLessonListController {
    @Autowired
    private AdminLessonService lessonService;
 // 管理者のレッスン一覧画面を表示する処理
    @GetMapping("/admin/lesson/all")
    public String listLessons(Model model, HttpSession session) {
    	 // セッションからログイン中の管理者情報を取得
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
     // 未ログインの場合、ログインページへリダイレクト
        if (admin == null) {
            return "redirect:/admin/login";
        }
        Long adminId = admin.getAdminId();
      // 管理者に紐づく全レッスンを取得
        List<Lesson> lessonList = lessonService.selectAllLessonList(adminId);
     // レッスン一覧を Thymeleaf テンプレートへ渡す
        model.addAttribute("lessonList", lessonList);
     // 管理者名もビューに渡して、画面に表示可能にする
        model.addAttribute("adminName", admin.getAdminName());
        return "admin_lesson_lineup";
    }
    @GetMapping("/admin/lesson/search")
	public String searchLesson(@RequestParam String keyword, Model model,HttpSession session) {
    Admin admin = (Admin) session.getAttribute("loginAdminInfo");
	    if (admin == null) {
        return "redirect:/admin/login";
	    }
	     Long adminId = admin.getAdminId();
    
	     List<Lesson> lessonList;

    if (keyword != null && !keyword.isBlank()) {
        // キーワードで部分一致検索（サービス側で実装）
        lessonList = lessonService.searchLessonsByName(adminId, keyword);
    } else {
        // 全件取得
        lessonList = lessonService.selectAllLessonList(adminId);
    }

    model.addAttribute("lessonList", lessonList);
    model.addAttribute("adminName", admin.getAdminName());
    model.addAttribute("keyword", keyword); // 検索語を再表示するため

    return "admin_lesson_lineup";
}
}
