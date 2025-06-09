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
	// UsersService をDI（依存性注入）して、ユーザー登録の処理を委譲する
		@Autowired
		private AdminService adminService;
		//ユーザー登録画面の表示
		@GetMapping("/admin/register")
		public String getAdminRegisterPage() {
			return "admin_register";
		}
		
	    //ユーザー登録処理
		@PostMapping("/admin/register/process")
		public String adminRegisterProcess(@RequestParam String adminName, @RequestParam String adminEmail,
				@RequestParam String adminpassword) {
			// ユーザー作成に成功した場合はログイン画面へリダイレクト
			if (adminService.createAccount(adminEmail, adminName, adminpassword)) {
				return "redirect:/admin/login";

			} else {
				 // 失敗した場合は登録画面に戻る
				return "admin_register";
			}
		}


			@PostMapping("/admin/confirm/register")

			public String adminRegisterConfirm(
			        @RequestParam String adminName,
			        @RequestParam String adminEmail,
			        @RequestParam String adminPassword,
			        Model model) {

			   
			    model.addAttribute("adminName", adminName);
			    model.addAttribute("adminEmail", adminEmail);
			    model.addAttribute("adminPassword", adminPassword); 


			    return "admin_confirm_register"; 
			
		}
}

