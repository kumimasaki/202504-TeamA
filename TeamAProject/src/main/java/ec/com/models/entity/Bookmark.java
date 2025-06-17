package ec.com.models.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

//ユーザーと講座のID、そしてブックマークした日時を記録します。
@Entity
@Table(name = "lesson_bookmark", uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "lesson_id" }))
public class Bookmark {

	// 各ブックマークの一意識別子
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 「Users」エンティティと関連付け。各ブックマークをどのユーザーが作成したかを示します。
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private Users user;

	// ブックマーク対象の講座ID。必要に応じて「Lesson」エンティティと関連づけられますが、
	// ここでは簡単にIDのみを保存します。
	@Column(name = "lesson_id", nullable = false)
	private Long lessonId;

	// ブックマークした日時
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	// デフォルトのコンストラクタ
	public Bookmark() {
	}

	// コンストラクタ

	public Bookmark(Users user, Long lessonId, LocalDateTime createdAt) {
		this.user = user;
		this.lessonId = lessonId;
		this.createdAt = createdAt;
	}

	// Getter,Setter

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}