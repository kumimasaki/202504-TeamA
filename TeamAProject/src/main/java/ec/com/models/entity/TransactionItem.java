package ec.com.models.entity;

import jakarta.persistence.*;

@Entity
public class TransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false) 
    private Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false) 
    private TransactionHistory transactionHistory;

    // コンストラクタ
    public TransactionItem() {}

    public TransactionItem(Lesson lesson, TransactionHistory transactionHistory) {
        this.lesson = lesson;
        this.transactionHistory = transactionHistory;
    }

    // getter, setter
    public Long getId() {
        return id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(TransactionHistory transactionHistory) {
        this.transactionHistory = transactionHistory;
    }
}
