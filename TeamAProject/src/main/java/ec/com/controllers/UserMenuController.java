package ec.com.controllers;

import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;
import ec.com.models.entity.Users;
import ec.com.services.BookmarkService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserMenuController {

    @Autowired
    private HttpSession session;

    @Autowired
    private LessonDao lessonDao;

    @Autowired
    private BookmarkService bookmarkService;

     //ユーザーメニュー画面を表示する
     //ログインユーザーの情報を取得し、ユーザー名とブックマーク済み講座IDリストを設定
     //キーワード検索があれば、講座名に含まれる講座の一覧を取得し、なければ全講座を取得
     //開始日が今日以降の講座のみ画面に表示

    @GetMapping("/user/menu")
    public String showMenu(Model model, @RequestParam(required = false) String keyword) {
        // ログイン判定
        Users loginUser = (Users) session.getAttribute("loginUser");
        boolean loginFlg = (loginUser != null);
        model.addAttribute("loginFlg", loginFlg);
        if (loginFlg) {
            model.addAttribute("userName", loginUser.getUserName());
            List<Long> bookmarkedIds = bookmarkService.getBookmarkedLessonIds(loginUser);
            model.addAttribute("bookmarkedIds", bookmarkedIds);
        }

        // 講座一覧の取得（キーワード検索があれば検索結果、なければ全件取得）
        List<Lesson> lessonList;
        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                lessonList = lessonDao.findByLessonNameContainingIgnoreCase(keyword.trim());
                model.addAttribute("keyword", keyword.trim());
            } else {
                lessonList = lessonDao.findAllByOrderByStartDateAscStartTimeAsc();
            }
        } catch (Exception e) {
            lessonList = Collections.emptyList();
        }

        // 開催日が今日より前の講座は表示しない
        LocalDate today = LocalDate.now();
        lessonList = lessonList.stream()
                .filter(l -> l.getStartDate() != null && !l.getStartDate().isBefore(today))
                .collect(Collectors.toList());

        model.addAttribute("lessonList", lessonList);
        return "user_menu.html";
    }
}
