package ec.com.models.dao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
public interface LessonDao extends JpaRepository<Lesson, Long> {
	// 保存処理と更新処理　insertとupdate
    Lesson save(Lesson lesson);
    
    // SELECT * FROM lesson ORDER BY start_date, start_time
    // 用途：講座一覧の表示に使用
    List<Lesson> findAllByOrderByStartDateAscStartTimeAsc();
    
    // SELECT * FROM lesson WHERE lesson_id = ?
    // 用途：講座詳細の表示に使用
    Lesson findByLessonId(Long lessonId);
    
    // SELECT * FROM lesson WHERE lesson_name = ?
    // 用途：講座名の重複チェックに使用
    Lesson findByLessonName(String lessonName);
    
    // DELETE FROM lesson WHERE lesson_id = ?
    // 用途：講座の削除に使用
    void deleteByLessonId(Long lessonId);
    
    // UPDATE lesson SET image_name = ? WHERE lesson_id = ?
    // 用途：講座画像の更新に使用
    void updateImageNameByLessonId(String imageName, Long lessonId);
}
