package ec.com.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ec.com.models.entity.Admin;
import ec.com.models.entity.Lesson;
import ec.com.services.AdminLessonService;
import jakarta.servlet.http.HttpSession;



@Controller
public class AdminLessonListController {
    @Autowired
    private AdminLessonService lessonService;

    @GetMapping("/admin/lesson/all")
    public String listLessons(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        Long adminId = admin.getAdminId();
        List<Lesson> lessonList = lessonService.selectAllLessonList(adminId);
        model.addAttribute("lessonList", lessonList);
        return "admin_lesson_lineup";
    }

}
