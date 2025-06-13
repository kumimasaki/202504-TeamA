

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

        private String saveImage(MultipartFile file) throws IOException {
            if (file != null && !file.isEmpty()) {
             
                String imageName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                        + "-" + file.getOriginalFilename();

              
                Path uploadDir = Paths.get("images");
                Files.createDirectories(uploadDir);
                 

                
                Path filePath = uploadDir.resolve(imageName);
                Files.copy(file.getInputStream(), filePath);

                return imageName;
            }
            return null;
        }
    }


