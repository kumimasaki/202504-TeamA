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

    @GetMapping("/admin/register")
    public String getAdminRegisterPage() {
        return "admin_register";
    }

    @PostMapping("/admin/confirm/register")
    public String adminRegisterConfirm(@RequestParam String adminName,
                                       @RequestParam String adminEmail,
                                       @RequestParam("password") String password,
                                       @RequestParam("adminPassword") String confirmPassword,
                                       Model model) {

        if (adminName.isBlank() || adminEmail.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "すべての項目を入力してください。");
            model.addAttribute("adminName", adminName);
            model.addAttribute("adminEmail", adminEmail);
            return "admin_register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "パスワードが一致しません。");
            model.addAttribute("adminName", adminName);
            model.addAttribute("adminEmail", adminEmail);
            return "admin_register";
        }

        model.addAttribute("adminName", adminName);
        model.addAttribute("adminEmail", adminEmail);
        model.addAttribute("adminPassword", password); // 本番環境では非表示またはハッシュ化処理推奨
        return "admin_confirm_register";
    }

    @PostMapping("/admin/register/complete")
    public String adminRegisterComplete(@RequestParam String adminName,
                                        @RequestParam String adminEmail,
                                        @RequestParam("adminPassword") String password,
                                        Model model) {

        boolean result = adminService.createAccount(adminName, adminEmail, password);

        if (!result) {
            model.addAttribute("error", "登録に失敗しました。メールアドレスが既に存在しています。");
            model.addAttribute("adminName", adminName);
            model.addAttribute("adminEmail", adminEmail);
            return "admin_register";
        }

        return "redirect:/admin/login";
    }
}
