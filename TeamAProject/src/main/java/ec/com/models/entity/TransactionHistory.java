package ec.com.models.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionHistory {
	// transaction_historyの設定
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionId;
	@ManyToOne
    @JoinColumn(name = "user_id") 
    private Users user;
	private Integer amount;
	private LocalDateTime transactionDate;

	// コンストラクタ
	public TransactionHistory() {
	}

	public TransactionHistory(Users user, Integer amount, LocalDateTime transactionDate) {
		this.user = user;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	// getter,setter
	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

}
