package ec.com.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.models.entity.TransactionItem;

public interface TransactionItemDao extends JpaRepository<TransactionItem, Long> {
}
