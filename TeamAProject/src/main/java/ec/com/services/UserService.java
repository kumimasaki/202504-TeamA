package ec.com.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.models.dao.UserDao;
import ec.com.models.entity.Users;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	// 登録処理
	public boolean createUser(String userName, String userEmail, String userPassword) {
		// 登録メールが重複しているかどうか
		if (userDao.findByUserEmail(userEmail) == null) {

			// もしメールが重複していない場合、保存
			// now 調査事項
			userDao.save(new Users(userName, userEmail, userPassword, 0, LocalDateTime.now()));

			return true;
		} else {
			return false;
		}
	}

	// ログイン処理
	public Users loginCheck(String userEmail, String userPassword) {
		Users account = userDao.findByUserEmailAndUserPassword(userEmail, userPassword);
		if (account == null) {
			return null;
		} else {
			return account;
		}
	}

}
