package at.lernplattform.application.courses;

public class UpdateCourseCommand {

	private String courseId;
	private String name;
	private String user;

	public UpdateCourseCommand(String courseId, String name, String user) {
		this.courseId = courseId;
		this.name = name;
		this.user = user;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getName() {
		return name;
	}

	public String getUser() {
		return user;
	}

}
