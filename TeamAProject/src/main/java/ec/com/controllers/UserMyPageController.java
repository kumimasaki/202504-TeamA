package ec.com.controllers;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ec.com.models.dao.LessonDao;
import ec.com.models.dao.TransactionHistoryDao;
import ec.com.models.dao.TransactionItemDao;
import ec.com.models.entity.TransactionHistory;
import ec.com.models.entity.TransactionItem;
import ec.com.models.entity.Users;
import ec.com.services.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserMyPageController {

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

	@GetMapping("/lesson/mypage")
	public String getMyPage(HttpSession session, Model model) {
		// sessionからログインしてるユーザーを取る
		Users loginUser = (Users) session.getAttribute("loginUser");
		boolean loginFlg = (loginUser != null);
		model.addAttribute("loginFlg", loginFlg);

		// ログインしてなかったらログイン画面に戻る
		if (!loginFlg) {
			return "user_login.html";
		}

		// ユーザー名をセットする
		model.addAttribute("userName", loginUser.getUserName());

		// ユーザーの購入したものを取ってくる
		List<TransactionHistory> historyList = transactionHistoryRepository.findByUser(loginUser);

		// 取ってきた購入記録から、レッスンの情報をまとめる
		List<TransactionItem> itemList = transactionItemRepository.findByTransactionHistory_User(loginUser);

		
		for (TransactionItem item : itemList) {
		    if (item.getLesson() != null && item.getLesson().getLessonFee() != null) {
		        int fee = item.getLesson().getLessonFee();
		        String formattedFee = NumberFormat.getNumberInstance(Locale.JAPAN).format(fee);
		        item.getLesson().setLessonDetail(formattedFee); // ✅ 临时塞进 lessonDetail 字段示范（也可以扩展实体）
		    }
		}

		model.addAttribute("listSub", itemList);


		return "mypage.html";
	}

	@PostMapping("/lesson/history/delete")
	@Transactional
	public String deleteTransactionItem(@RequestParam("itemId") Long itemId) {
	    transactionItemRepository.deleteById(itemId);
	    return "redirect:/lesson/mypage";
	}

}