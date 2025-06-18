package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.entity.Admin;
import ec.com.models.entity.Lesson;
import ec.com.services.AdminLessonService;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminLessonDeleteController {
	// AdminLessonService を自動注入
    @Autowired
    private AdminLessonService adminLessonService;
    // セッション情報を取得するための HttpSession を自動注入
    @Autowired
    private HttpSession session;
    // レッスン削除一覧画面の表示処理（GETリクエスト）
    @GetMapping("/admin/lesson/delete")
    public String showLessonDeleteList(Model model) {
    	// 管理者がログインしているかを確認（セッションから取得）
        Object adminObj = session.getAttribute("loginAdminInfo");
        if (adminObj == null) {
        	// 未ログインならログイン画面へリダイレクト
            return "redirect:/admin/login";
        }
        // セッションから管理者情報を取得
        Admin admin = (Admin) adminObj;
        String adminName = admin.getAdminName();
        model.addAttribute("adminName", adminName);
        // 管理者IDに紐づくレッスン一覧を取得し、モデルに追加
        List<Lesson> lessonList = adminLessonService.selectAllLessonList(admin.getAdminId());
        model.addAttribute("lessonList", lessonList);

        return "admin_delete_lesson";
    }
      // レッスンの削除処理
    @PostMapping("/admin/lesson/delete/remove")
    public String deleteLesson(@RequestParam Long lessonId) {
    	 // セッションからログイン中の管理者情報を取得
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }
     // レッスンを削除し、成功したら一覧ページへ、失敗したら削除画面へ戻る
        boolean success = adminLessonService.deleteLesson(lessonId);
        return success ? "redirect:/admin/lesson/all" : "redirect:/admin/lesson/delete";
    }
} 