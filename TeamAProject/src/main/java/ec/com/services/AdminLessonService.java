package ec.com.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;

@Service

public class AdminLessonService {
	@Autowired
	private LessonDao lessonDao;

	public List<Lesson> selectAllLessonList(Long adminId) {
		if (adminId == null) {
			return null;
		} else {
			return lessonDao.findAll();
		}

	}

	// 新しいLesson記事を作成する
	public boolean createLesson(LocalDate startDate, LocalTime startTime, LocalTime finishTime,String lessonDetail,
			String lessonName, Integer lessonFee, String imageName, LocalDateTime registerDate,Long adminId) {
		// タイトルが重複していない場合のみ保存
		if (lessonDao.findByLessonName(lessonName) == null) {
			lessonDao.save(new Lesson(startDate, startTime, finishTime, lessonName, lessonDetail, lessonFee, imageName,
					registerDate));
			return true;
		} else {
			return false;
		}

	}
}