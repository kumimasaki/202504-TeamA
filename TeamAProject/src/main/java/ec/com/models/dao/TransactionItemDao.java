package ec.com.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ec.com.models.entity.TransactionItem;
import ec.com.models.entity.Users;

public interface TransactionItemDao extends JpaRepository<TransactionItem, Long> {
	
	List<TransactionItem> findByTransactionHistory_User(Users user);
	
	@Modifying
    @Query("delete from TransactionItem ti where ti.transactionHistory.transactionId = :transactionId")
    void deleteByTransactionHistoryId(@Param("transactionId") Long transactionId);

}
