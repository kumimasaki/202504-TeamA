package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;

import ec.com.models.entity.Admin;
import ec.com.models.entity.Lesson;
import ec.com.services.AdminLessonEditService;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin/lesson")
public class AdminLessonEditImageController {
    @Autowired
    private AdminLessonEditService adminLessonEditService;
    
    @Autowired
    private HttpSession session;

    @GetMapping("/image/edit/{lessonId}")
    public String showEditImageForm(@PathVariable Long lessonId, Model model) {
        // セッションからログイン中のユーザーを取得
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        // 未ログインならログイン画面へリダイレクト
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        Lesson lesson = adminLessonEditService.getLessonById(lessonId);
        model.addAttribute("lesson", lesson);
        model.addAttribute("adminName", admin.getAdminName());
        return "admin_edit_lesson_img";
    }

    @PostMapping("/image/edit/update")
    public String updateLessonImage(
        @RequestParam("imageName") MultipartFile imageFile,
        @RequestParam("lessonId") Long lessonId,
        @RequestParam("lessonName") String lessonName,
        @RequestParam("lessonDetail") String lessonDetail,
        @RequestParam("lessonFee") Integer lessonFee,
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("startTime") LocalTime startTime,
        @RequestParam("finishTime") LocalTime finishTime
    ) {
        Admin admin = (Admin) session.getAttribute("loginAdminInfo");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonId);
        lesson.setLessonName(lessonName);
        lesson.setLessonDetail(lessonDetail);
        lesson.setLessonFee(lessonFee);
        lesson.setStartDate(startDate);
        lesson.setStartTime(startTime);
        lesson.setFinishTime(finishTime);

        if (adminLessonEditService.updateLessonImage(imageFile, lesson)) {
            return "redirect:/admin/lesson/all";
        } else {
            return "admin_edit_lesson_img";
        }
    }
} 