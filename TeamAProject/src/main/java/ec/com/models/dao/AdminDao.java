package ec.com.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.teamA.entity.Admin;
import jakarta.transaction.Transactional;
public interface AdminDao extends JpaRepository<Admin, Long> {
	// 保存処理と更新処理　insertとupdate
    Admin save(Admin admin);
    
    // SELECT * FROM admin WHERE admin_email = ? AND delete_flg = 0
    // 用途：ログイン時に使用
    Admin findByAdminEmailAndDeleteFlg(String adminEmail, Integer deleteFlg);
    
    // SELECT * FROM admin WHERE admin_id = ? AND delete_flg = 0
    // 用途：管理者情報の取得に使用
    Admin findByAdminIdAndDeleteFlg(Long adminId, Integer deleteFlg);
    
    // UPDATE admin SET delete_flg = 1 WHERE admin_id = ?
    // 用途：管理者の論理削除に使用
    void deleteByAdminId(Long adminId);
}
