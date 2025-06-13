package ec.com.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.models.entity.TransactionItem;
import ec.com.models.entity.Users;

public interface TransactionItemDao extends JpaRepository<TransactionItem, Long> {
	
	List<TransactionItem> findByTransactionHistory_User(Users user);
}
