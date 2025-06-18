package ec.com.models.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long commentId;
	@ManyToOne
	private Users user;
	@ManyToOne
	private Lesson lesson;
	private String content;
	private LocalDateTime createdAt;

	public Comment() {
	}

	public Comment(Users user, Lesson lesson, String content, LocalDateTime createdAt) {
		this.user = user;
		this.lesson = lesson;
		this.content = content;
		this.createdAt = createdAt;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long id) {
		this.commentId = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
