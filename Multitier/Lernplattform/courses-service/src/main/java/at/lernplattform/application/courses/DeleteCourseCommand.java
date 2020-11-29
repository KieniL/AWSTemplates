package at.lernplattform.application.courses;

public class DeleteCourseCommand {

	private String userId;
	private String courseId;

	public DeleteCourseCommand(String userId, String courseId) {
		this.userId = userId;
		this.courseId = courseId;
	}

	public String getUserId() {
		return userId;
	}

	public String getCourseId() {
		return courseId;
	}

}
