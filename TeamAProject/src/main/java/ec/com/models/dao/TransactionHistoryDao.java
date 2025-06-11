package ec.com.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.models.entity.TransactionHistory;

public interface TransactionHistoryDao extends JpaRepository<TransactionHistory, Long> {
}