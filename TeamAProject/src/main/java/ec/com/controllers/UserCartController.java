package ec.com.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserCartController {

	private final LessonDao lessonDao;

	public UserCartController(LessonDao lessonDao) {
		this.lessonDao = lessonDao;
	}

	// カート一覧機能
	@GetMapping("/lesson/show/cart")
	public String showCart(HttpSession session, Model model) {
		// セッションからcartを取得する
		List<Long> cart = (List<Long>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
		}
		// レッスンIDリストからLessonオブジェクトのリストを取得する
		List<Lesson> lessonsInCart = lessonDao.findAllById(cart);

		model.addAttribute("list", lessonsInCart);
		Boolean loginFlg = (Boolean) session.getAttribute("loginFlg");
		if (loginFlg == null) {
			loginFlg = false;
		}
		model.addAttribute("loginFlg", loginFlg);

		return "user_planned_application";
	}

	// カート登録機能
	@PostMapping("/lesson/cart/all")
	public String addToCart(@RequestParam("lessonId") Long lessonId, HttpSession session) {
		Lesson lesson = lessonDao.findById(lessonId).orElse(null);
		if (lesson == null) {
			return "redirect:/lesson/show/cart";
		}

		LocalDate today = LocalDate.now();
		// 開催日が今日より前、または未設定の場合はカートに追加しない
		if (lesson.getStartDate() == null || lesson.getStartDate().isBefore(today)) {
			return "redirect:/lesson/show/cart";
		}
		// カートが未作成の場合、新しく作る
		List<Long> cart = (List<Long>) session.getAttribute("cart");
		if (cart == null) {
			cart = new ArrayList<>();
		}
		// カートに未登録なら追加
		if (!cart.contains(lessonId)) {
			cart.add(lessonId);
		}

		session.setAttribute("cart", cart);
		return "redirect:/lesson/show/cart";
	}

	// カート削除機能
	@GetMapping("/lesson/cart/delete/{lessonId}")
	public String deleteFromCart(@PathVariable Long lessonId, HttpSession session) {
		List<Long> cart = (List<Long>) session.getAttribute("cart");
		if (cart != null && cart.contains(lessonId)) {
			cart.remove(lessonId);
			session.setAttribute("cart", cart);
		}
		return "redirect:/lesson/show/cart";
	}

}
