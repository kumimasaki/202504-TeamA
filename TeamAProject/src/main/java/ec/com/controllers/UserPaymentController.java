package ec.com.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.dao.LessonDao;
import ec.com.models.dao.TransactionHistoryDao;
import ec.com.models.dao.TransactionItemDao;
import ec.com.models.entity.Lesson;
import ec.com.models.entity.TransactionHistory;
import ec.com.models.entity.TransactionItem;
import ec.com.models.entity.Users;
import ec.com.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserPaymentController {

	@Autowired
	private HttpSession session;

	@Autowired
	private LessonDao lessonDao;

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionHistoryDao transactionHistoryRepository;

	@Autowired
	private TransactionItemDao transactionItemRepository;

	// 購入申請画面を表示（支払い方法を選ぶ）
	@GetMapping("/lesson/request")
	public String paymentRequest(HttpSession session, Model model) {
		// ログインユーザー情報を取得
		Users loginUser = (Users) session.getAttribute("loginUser");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);

		// 未ログインならログイン画面へ
		if (!loginFlg) {
			return "user_login.html";
		}

		// ユーザー名を表示用に追加
		model.addAttribute("userName", loginUser.getUserName());

		// カートの中身を取得
		List<Long> cart = (List<Long>) session.getAttribute("cart");

		// カートが空なら戻す
		if (cart == null || cart.isEmpty()) {
			return "redirect:/lesson/show/cart";
		}

		// カートにある講座IDから講座リストを取得
		List<Lesson> list = lessonDao.findAllById(cart);
		model.addAttribute("list", list);

		return "user_apply_select_payment.html";
	}

	// 支払い確認画面（支払い方法の確認）
	@PostMapping("/lesson/confirm")
	public String payMentConfirm(HttpSession session, Model model, @RequestParam(value = "payment", required = false) String paymentValue) {
		// ログインチェック
		Users loginUser = (Users) session.getAttribute("loginUser");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);

		// 未ログインならログイン画面へ
		if (loginFlg) {
			model.addAttribute("userName", loginUser.getUserName());
		} else {
			return "user_login.html";
		}
		if (paymentValue == null || paymentValue.isEmpty()) {
			// カートの中身を取得
			List<Long> cart = (List<Long>) session.getAttribute("cart");
			// カートにある講座IDから講座リストを取得
			List<Lesson> list = lessonDao.findAllById(cart);
			model.addAttribute("list", list);
		    return "user_apply_select_payment.html";
		}else {
		// 支払い方法を日本語で表示用に変換
		String payMethodText = "";
		switch (paymentValue) {
		case "0":
			payMethodText = "当日現金支払い";
			break;
		case "1":
			payMethodText = "事前銀行振込";
			break;
		case "2":
			payMethodText = "クレジットカード決済";
			break;
		}
		// カートの中身を取得
		List<Long> cart = (List<Long>) session.getAttribute("cart");
		// カートにある講座IDから講座リストを取得
		List<Lesson> list = lessonDao.findAllById(cart);
		model.addAttribute("list", list);
		// 合計金額を計算
		int totalAmount = list.stream().mapToInt(Lesson::getLessonFee).sum();
		model.addAttribute("amount", totalAmount);
		model.addAttribute("payMethod", payMethodText);
		model.addAttribute("payFlg", false); // 確認画面なので支払いはまだ完了していない

		return "user_confirm_apply_detail.html";}
	}

	// 支払い完了処理（購入データ保存）
	@GetMapping("/lesson/complete")
	public String paymentSuccess(HttpSession session, Model model) {
		// ログインユーザー確認
		Users loginUser = (Users) session.getAttribute("loginUser");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);

		// ログインしていない場合はログイン画面へ
		if (!loginFlg) {
			return "user_login.html";
		}
		model.addAttribute("userName", loginUser.getUserName());
		// カートの中身確認
		List<Long> cart = (List<Long>) session.getAttribute("cart");
		if (cart == null || cart.isEmpty()) {
			// カートが空なら講座一覧に戻す
			return "redirect:/lesson/show/cart";
		}

		// カートに入っている講座を取得
		List<Lesson> lessons = lessonDao.findAllById(cart);
		// 合計金額を計算
		int totalAmount = lessons.stream().mapToInt(Lesson::getLessonFee).sum();
		// 購入履歴（トランザクション）を保存
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.setUser(loginUser);
		transactionHistory.setAmount(totalAmount);
		transactionHistory.setTransactionDate(LocalDateTime.now());
		transactionHistoryRepository.save(transactionHistory);

		// 各講座ごとに明細データを保存
		for (Long lessonId : cart) {
			Optional<Lesson> lessonOpt = lessonDao.findById(lessonId);
			if (lessonOpt.isPresent()) {
				TransactionItem item = new TransactionItem();
				item.setLesson(lessonOpt.get());
				item.setTransactionHistory(transactionHistory);
				transactionItemRepository.save(item);
			} else {
				System.out.println("講座がありません" + lessonId);
			}
		}

		// カートを空にする
		session.removeAttribute("cart");
		return "user_apply_complete.html";
	}
}