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

    @Autowired
    private AdminLessonService adminLessonService;

    @Autowired
    private HttpSession session;

    @GetMapping("/admin/lesson/delete")
    public String showLessonDeleteList(Model model) {
        Object adminObj = session.getAttribute("loginAdminInfo");
        if (adminObj == null) {
            return "redirect:/admin/login";
        }

        Admin admin = (Admin) adminObj;
        String adminName = admin.getAdminName();
        model.addAttribute("adminName", adminName);

        List<Lesson> lessonList = adminLessonService.selectAllLessonList(admin.getAdminId());
        model.addAttribute("lessonList", lessonList);

        return "admin_delete_lesson";
    }

    @PostMapping("/admin/lesson/delete/remove")
    public String deleteLesson(@RequestParam Long lessonId) {
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        boolean success = adminLessonService.deleteLesson(lessonId);
        return success ? "redirect:/admin/lesson/all" : "redirect:/admin/lesson/delete";
    }
} 