package ec.com.models.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Lesson {
	// lessonの設定
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lessonId;
	private LocalDate startDate;
	private LocalTime startTime;
	private LocalTime finishTime;
	private String lessonName;
	private String lessonDetail;
	private Integer lessonFee;
	private String imageName;
	private LocalDateTime registerDate;
	private Long adminId;
	@OneToMany(mappedBy = "lesson", cascade = CascadeType.REMOVE)
	private List<Comment> comments;

	// コンストラクタ
	public Lesson() {
	}

	public Lesson(LocalDate startDate, LocalTime startTime, LocalTime finishTime, String lessonName,
			String lessonDetail, Integer lessonFee, String imageName, LocalDateTime registerDate, Long adminId) {
		this.startDate = startDate;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.lessonName = lessonName;
		this.lessonDetail = lessonDetail;
		this.lessonFee = lessonFee;
		this.imageName = imageName;
		this.registerDate = registerDate;
		this.adminId = adminId;

	}
	// getter,setter

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getLessonId() {
		return lessonId;
	}

	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getLessonDetail() {
		return lessonDetail;
	}

	public void setLessonDetail(String lessonDetail) {
		this.lessonDetail = lessonDetail;
	}

	public Integer getLessonFee() {
		return lessonFee;
	}

	public void setLessonFee(Integer lessonFee) {
		this.lessonFee = lessonFee;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

}