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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;

@Service
public class AdminLessonService {

    @Autowired
    private LessonDao lessonDao;

    private final String UPLOAD_DIR = "src/main/resources/static/images/";

    public List<Lesson> selectAllLessonList(Long adminId) {
        if (adminId == null) {
            return null;
        } else {
            return lessonDao.findAll();
        }
    }

    public boolean createLesson(LocalDate startDate, LocalTime startTime, LocalTime finishTime,
                                 String lessonDetail, String lessonName, Integer lessonFee,
                                 MultipartFile imageFile, LocalDateTime registerDate, Long adminId) {
        if (lessonDao.findByLessonName(lessonName) != null) {
            return false;
        }

        String imageName = null;
        try {
            imageName = saveImage(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Lesson lesson = new Lesson(startDate, startTime, finishTime, lessonName, lessonDetail,
                                    lessonFee, imageName, registerDate, adminId);
        lessonDao.save(lesson);
        return true;
    }

    public boolean lessonUpdate(LocalDate startDate, LocalTime startTime, LocalTime finishTime,
                                 String lessonDetail, String lessonName, Integer lessonFee,
                                 MultipartFile imageFile, LocalDateTime registerDate,
                                 Long lessonId, Long adminId) {
        if (lessonId == null) {
            return false;
        }

        Lesson lesson = lessonDao.findByLessonId(lessonId);
        lesson.setLessonName(lessonName);
        lesson.setLessonDetail(lessonDetail);
        lesson.setStartDate(startDate);
        lesson.setStartTime(startTime);
        lesson.setFinishTime(finishTime);
        lesson.setLessonFee(lessonFee);
        lesson.setRegisterDate(registerDate);

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

    public Lesson lessonEditCheck(Long lessonId) {
        if (lessonId == null) {
            return null;
        }
        return lessonDao.findByLessonId(lessonId);
    }

    public boolean deleteLesson(Long lessonId) {
        if (lessonId == null) {
            return false;
        }

        Lesson lesson = lessonDao.findByLessonId(lessonId);
        if (lesson == null) {
            return false;
        }

        // 先尝试删除图片文件
        String imageName = lesson.getImageName();
        if (imageName != null && !imageName.isEmpty()) {
            Path imagePath = Paths.get(UPLOAD_DIR, imageName);
            try {
                if (Files.exists(imagePath)) {
                    Files.delete(imagePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false; // 如果图片删除失败，整个删除操作失败
            }
        }

        // 图片删除成功后，再删除数据库记录
        try {
            lessonDao.deleteByLessonId(lessonId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String imageName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                    + "-" + file.getOriginalFilename();
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(file.getInputStream(), uploadPath.resolve(imageName));
            return imageName;
        }
        return null;
    }
}
