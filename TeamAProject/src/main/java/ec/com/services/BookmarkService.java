package ec.com.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ec.com.models.dao.BookmarkDao;
import ec.com.models.entity.Bookmark;
import ec.com.models.entity.Users;

@Service
public class BookmarkService {

	@Autowired
	private BookmarkDao bookmarkDao;

	// もし既にブックマークされていれば削除を行い、されてなければ追加します。
	@Transactional
	public boolean toggleBookmark(Users user, Long lessonId) {
		if (bookmarkDao.existsByUserAndLessonId(user, lessonId)) {
			// 既にブックマークされている場合は削除
			bookmarkDao.deleteByUserAndLessonId(user, lessonId);
			return false;
		} else {
			// ブックマークしていない場合は新たに追加
			Bookmark bm = new Bookmark(user, lessonId, LocalDateTime.now());
			bookmarkDao.save(bm);
			return true;
		}
	}

	// ユーザーがブックマークしている講座IDのリストを取得します。
	public List<Long> getBookmarkedLessonIds(Users user) {
		return bookmarkDao.findBookmarkedLessonIds(user);
	}
}