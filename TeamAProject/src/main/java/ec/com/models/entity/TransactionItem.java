package ec.com.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionItem {
	// transaction_itemの設定
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private Lesson lesson;
	@ManyToOne
	private TransactionHistory transaction;

	// コンストラクタ
	public TransactionItem() {
	}

	public TransactionItem(Lesson lesson, TransactionHistory transaction) {
		this.lesson = lesson;
		this.transaction = transaction;
	}

	// getter,setter
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public TransactionHistory getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionHistory transaction) {
		this.transaction = transaction;
	}

}
