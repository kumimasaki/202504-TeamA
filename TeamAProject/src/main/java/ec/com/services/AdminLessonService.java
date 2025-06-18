package ec.com.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;
import jakarta.transaction.Transactional;

@Service
public class AdminLessonService {

	@Autowired
	private LessonDao lessonDao;
	// 管理者に関連する全レッスンを取得
	public List<Lesson> selectAllLessonList(Long adminId) {
		if (adminId == null) {
			return null;
		} else {
			return lessonDao.findAll();
		}
	}
	// レッスン新規登録処理
	public boolean createLesson(LocalDate startDate, LocalTime startTime, LocalTime finishTime, String lessonDetail,
			String lessonName, Integer lessonFee, MultipartFile imageFile, LocalDateTime registerDate, Long adminId) {
		 // 同名レッスンが既に存在する場合、登録不可
		if (lessonDao.findByLessonName(lessonName) != null) {
			return false;
		}

		String imageName = null;
		try {
		 // 画像を保存して、保存されたファイル名を取得
			imageName = saveImage(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		  // レッスン情報を組み立てて保存
		Lesson lesson = new Lesson(startDate, startTime, finishTime, lessonName, lessonDetail, lessonFee, imageName,
				registerDate, adminId);
		lessonDao.save(lesson);
		return true;
	}
	// レッスン情報の更新処理
	public boolean lessonUpdate(LocalDate startDate, LocalTime startTime, LocalTime finishTime, String lessonDetail,
			String lessonName, Integer lessonFee, MultipartFile imageFile, LocalDateTime registerDate, Long lessonId,
			Long adminId) {
		if (lessonId == null) {
			return false;
		}
		// 更新対象レッスンを取得
		Lesson lesson = lessonDao.findByLessonId(lessonId);
		// 各フィールドを更新
		lesson.setLessonName(lessonName);
		lesson.setLessonDetail(lessonDetail);
		lesson.setStartDate(startDate);
		lesson.setStartTime(startTime);
		lesson.setFinishTime(finishTime);
		lesson.setLessonFee(lessonFee);
		lesson.setRegisterDate(registerDate);
		// 新しい画像がある場合は上書き保存
		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				String imageName = saveImage(imageFile);
				lesson.setImageName(imageName);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		lesson.setAdminId(adminId);
		lessonDao.save(lesson);
		return true;
	}
	// レッスン詳細取得
	public Lesson lessonEditCheck(Long lessonId) {
		if (lessonId == null) {
			return null;
		}
		return lessonDao.findByLessonId(lessonId);
	}
	// レッスン削除処理（画像含む）
	@Transactional
	public boolean deleteLesson(Long lessonId) {
		if (lessonId == null) {
			return false;
		}

		Lesson lesson = lessonDao.findByLessonId(lessonId);
		if (lesson == null) {
			return false;
		}

		 // 関連画像ファイルの削除処理
		String imageName = lesson.getImageName();
		if (imageName != null && !imageName.isEmpty()) {
			Path imagePath = Paths.get("images", imageName);
			try {
				if (Files.exists(imagePath)) {
					Files.delete(imagePath);
				}
			} catch (IOException e) {
				e.printStackTrace();
				 // 画像削除失敗 → 処理中断
				return false; 
			}
		}

		 // 画像削除成功後にDBレコードを削除
		try {
			lessonDao.deleteByLessonId(lessonId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// アップロードされた画像ファイルを保存し、保存名を返す
	private String saveImage(MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			// 現在時刻 + 元ファイル名でユニークな名前を生成
			String imageName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + "-"
					+ file.getOriginalFilename();
			 // 保存先パスを作成
			Path uploadPath = Paths.get("images");

			Files.createDirectories(uploadPath);
			// ファイルをコピーして保存
			Path filePath = uploadPath.resolve(imageName);
			Files.copy(file.getInputStream(), filePath);

			return imageName;
		}
		return null;
	}
	public List<Lesson> searchLessonsByName(Long adminId, String keyword) {
	    return lessonDao.findByAdminIdAndLessonNameContaining(adminId, keyword);
	}
}
