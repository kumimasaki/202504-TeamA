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

    
    @GetMapping("/admin/lesson/edit/{lessonId}")
    public String getLessonEditPage(@PathVariable Long lessonId, Model model) {
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        Lesson lesson = adminLessonService.lessonEditCheck(lessonId);
        if (lesson == null) {
            return "redirect:/admin/lesson/all";
        }

        model.addAttribute("adminName", admin.getAdminName());
        model.addAttribute("lesson", lesson);
        return "admin_edit_lesson";
    }

    
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

        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        LocalDateTime registerDate = LocalDateTime.now();
        boolean success = adminLessonService.lessonUpdate(
                startDate, startTime, finishTime,
                lessonDetail, lessonName, lessonFee,
                imageName, registerDate, lessonId, admin.getAdminId());

        return success ? "admin_fix_register" : "redirect:/admin/lesson/edit/" + lessonId;
    }
}