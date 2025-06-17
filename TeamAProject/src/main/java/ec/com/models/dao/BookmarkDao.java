package ec.com.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ec.com.models.entity.Bookmark;
import ec.com.models.entity.Users;

public interface BookmarkDao extends JpaRepository<Bookmark, Long> {

	// 指定したユーザーが特定の講座をブックマークしているかどうかを判断します。
	boolean existsByUserAndLessonId(Users user, Long lessonId);

	// 指定したユーザーのブックマークした講座を削除します。
	void deleteByUserAndLessonId(Users user, Long lessonId);

	// 指定したユーザーのブックマーク情報リストを取得します。
	List<Bookmark> findByUser(Users user);

	// ユーザーがブックマークしている講座IDリストを取得します。 ※画面側でブックマーク済かどうかの判断に利用します。
	@Query("select b.lessonId from Bookmark b where b.user = :user")
	List<Long> findBookmarkedLessonIds(@Param("user") Users user);
}