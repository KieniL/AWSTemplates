package at.lernplattform.application.courses;

public class RestoreCourseCommand {

	private String courseId;
	private String user;

	public RestoreCourseCommand(String courseId,String user) {
		this.courseId = courseId;
		this.user = user;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getUser() {
		return user;
	}

}
