package ec.com.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ec.com.models.dao.LessonDao;
import ec.com.models.entity.Lesson;

@Service
public class AdminLessonEditService {
    @Autowired
    private LessonDao lessonDao;

   

    // 講座情報を取得する
    public Lesson getLessonById(Long lessonId) {
        if (lessonId == null) {
            return null;
        } else {
            return lessonDao.findByLessonId(lessonId);
        }
    }

    // 講座画像を更新する
    public boolean updateLessonImage(MultipartFile imageName, Lesson lesson) {
        if (imageName != null && !imageName.isEmpty() && lesson != null) {
            try {
                // 新しいファイル名を生成
                String originalFilename = imageName.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                
                String newFilename = UUID.randomUUID().toString() + extension;

                // 新しい画像を保存
                Path uploadPath = Paths.get("images");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(imageName.getInputStream(), uploadPath.resolve(newFilename));

                // 古い画像を削除
                if (lesson.getImageName() != null) {
                    Path oldImagePath = uploadPath.resolve(lesson.getImageName());
                    Files.deleteIfExists(oldImagePath);
                }

                // 講座情報を更新
                lesson.setImageName(newFilename);
                lessonDao.save(lesson);
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }
} 