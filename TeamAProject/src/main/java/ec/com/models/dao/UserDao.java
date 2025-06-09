package ec.com.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.com.models.entity.Users;

@Repository
public interface UserDao extends JpaRepository<Users, Long>{

	//ユーザー情報の保存（新規登録または更新）
	Users save(Users user);
	
	//メールアドレスでユーザーを検索用
	//ユーザー登録時に重複チェック用
	Users findByUserEmail(String userEmail);
	
	//メールアドレスとパスワードでユーザーを検索用
	//ログイン認証用
	Users findByUserEmailAndUserPassword(String userEmail, String userPassword);
}
