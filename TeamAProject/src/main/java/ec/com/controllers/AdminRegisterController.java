package ec.com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.services.AdminService;

@Controller
public class AdminRegisterController {

    @Autowired
    private AdminService adminService;
    // 登録フォーム画面の表示
    @GetMapping("/admin/register")
    public String getAdminRegisterPage() {
        return "admin_register";
    }
     // 入力内容の確認画面へ進む
    @PostMapping("/admin/register/process")
    public String adminRegisterConfirm(@RequestParam String adminName,
                                       @RequestParam String adminEmail,
                                       @RequestParam("password") String password,
                                       @RequestParam("adminPassword") String confirmPassword,
                                       Model model) {
    	// 入力必須チェック
        if (adminName.isBlank() || adminEmail.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "すべての項目を入力してください。");
            model.addAttribute("adminName", adminName);
            model.addAttribute("adminEmail", adminEmail);
            return "admin_register";
        }
        // パスワード一致確認
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "パスワードが一致しません。");
            model.addAttribute("adminName", adminName);
            model.addAttribute("adminEmail", adminEmail);
            return "admin_register";
        }
        

        // 確認画面に渡すデータを model に格納
        model.addAttribute("adminName", adminName);
        model.addAttribute("adminEmail", adminEmail);
        model.addAttribute("adminPassword", password); 
        return "admin_confirm_register";
       
        
    }
     // 確認後、本登録を実行する
    @PostMapping("/admin/confirm/process")
    public String adminRegisterComplete(@RequestParam String adminName,
                                        @RequestParam String adminEmail,
                                        @RequestParam("adminPassword") String password,
                                        Model model) {
    	 // サービスを使って DB に登録を試みる
        boolean result = adminService.createAccount(adminName, adminEmail, password);
     // 登録失敗
        if (!result) {
            model.addAttribute("error", "登録に失敗しました。メールアドレスが既に存在しています。");
            model.addAttribute("adminName", adminName);
            model.addAttribute("adminEmail", adminEmail);
            return "admin_register";
        }
     // 登録成功 
        return "admin_login";
    }
}
