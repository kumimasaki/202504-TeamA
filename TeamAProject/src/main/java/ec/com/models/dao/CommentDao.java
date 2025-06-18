package ec.com.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.models.entity.Comment;

public interface CommentDao extends JpaRepository<Comment, Long> {
	
	List<Comment> findByLesson_LessonIdOrderByCreatedAtDesc(Long lessonId);
}
