package ec.com.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.models.entity.TransactionHistory;
import ec.com.models.entity.Users;

public interface TransactionHistoryDao extends JpaRepository<TransactionHistory, Long> {
	
	List<TransactionHistory> findByUser(Users user);

}