package ec.com.controllers;

import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String showMenu(Model model) {
        //ログイン判定
        Users loginUser = (Users) session.getAttribute("loginUser");
        boolean loginFlg = (loginUser != null);
        model.addAttribute("loginFlg", loginFlg);
        if (loginFlg) {
            model.addAttribute("userName", loginUser.getUserName());
        }

        //講座一覧
        List<Lesson> lessonList;
        try {
            lessonList = lessonDao.findAllByOrderByStartDateAscStartTimeAsc();
        } catch (Exception e) {
            // DAOがまだ無い場合 画面だけ表示する
            lessonList = Collections.emptyList();
        }
        model.addAttribute("lessonList", lessonList);
        return "user_menu.html"; 
    }
}
