package ec.com.services;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.models.entity.Users;
import ec.com.models.dao.AdminDao;
import ec.com.models.entity.Admin;

@Service
public class AdminService {
	@Autowired
	private AdminDao adminDao;
	public boolean createAccount(String adminName,String adminEmail , String adminPassword) {
		// メールアドレスが未登録である場合に新規アカウント作成
		if (adminDao.findByAdminEmailAndDeleteFlg(adminEmail, 0) == null) {
			adminDao.save(new Admin(adminName, adminEmail, adminPassword, 0, OffsetDateTime.now()));
			return true;
		} else {
			// 同じメールアドレスが既に存在する場合は登録失敗
			return false;
		}
	}

           //ログインチェック処理
	public Admin loginCheck(String adminEmail, String adminPassword) {
		// メールアドレスとパスワードが一致するユーザーを検索
		Admin admin = adminDao.findByAdminEmailAndAdminPassword(adminEmail,adminPassword);
		if (admin == null) {
			return null;
		} else {
			return admin;
		}
	}
}
