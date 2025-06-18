package ec.com.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.models.dao.CommentDao;
import ec.com.models.entity.Comment;
import ec.com.models.entity.Lesson;
import ec.com.models.entity.Users;

@Service
public class CommentService {

	@Autowired
	private CommentDao commentDao;

	// 指定講座のコメント一覧を取得
	public List<Comment> getCommentsByLessonId(Long lessonId) {
		return commentDao.findByLesson_LessonIdOrderByCreatedAtDesc(lessonId);
	}

	// コメントを追加する
	public void addComment(Lesson lesson, Users user, String content) {
		Comment comment = new Comment();
		comment.setLesson(lesson);
		comment.setUser(user);
		comment.setContent(content);
		comment.setCreatedAt(LocalDateTime.now());
		commentDao.save(comment);
	}
}
