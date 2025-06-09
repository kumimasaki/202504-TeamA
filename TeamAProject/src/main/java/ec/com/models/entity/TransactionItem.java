package ec.com.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionItem {
	// transaction_itemの設定
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long lessonid;
	private Long transactionId;

	// コンストラクタ
	public TransactionItem() {
	}

	public TransactionItem(Long lessonid, Long transactionId) {
		this.lessonid = lessonid;
		this.transactionId = transactionId;
	}

	// getter,setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLessonid() {
		return lessonid;
	}

	public void setLessonid(Long lessonid) {
		this.lessonid = lessonid;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

}
