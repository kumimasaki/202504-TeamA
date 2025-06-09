package ec.com.controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
@RequestMapping("/admin")
public class AdminLoginController {
	@Autowired
    private AdminDao adminDao;

    // 管理者ログイン画面を表示
    @GetMapping("/login")
    public String showLoginForm() {
        return "admin_login";
    }

    // 管理者ログイン処理
    @PostMapping("/login")
    public String processLogin(@RequestParam String adminEmail, 
                             @RequestParam String adminPassword,
                             HttpSession session,
                             Model model) {
        try {
            // メールアドレスと削除フラグで管理者を検索
            Admin admin = adminDao.findByAdminEmailAndDeleteFlg(adminEmail, 0);
            
            // 管理者が存在し、パスワードが一致する場合
            if (admin != null && admin.getAdminPassword().equals(adminPassword)) {
                // セッションに管理者情報を保存
                session.setAttribute("admin", admin);
                // 講座一覧画面にリダイレクト
                return "redirect:/admin/lesson/list";
            }
        } catch (Exception e) {
            // エラーログを出力
            e.printStackTrace();
        }
        
        // ログイン失敗時のエラーメッセージを設定
        model.addAttribute("error", "メールアドレスまたはパスワードが正しくありません。");
        return "admin_login";
    }

    // 管理者ログアウト処理
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // セッションを無効化
        session.invalidate();
        // ログイン画面にリダイレクト
        return "redirect:/admin/login";
    }
}
