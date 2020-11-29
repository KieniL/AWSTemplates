package at.lernplattform.application.tasks;

public class CreateTaskCommand {

	private String userId;
	private String courseId;
	private String name;
	private String description;
	private String code;

	public CreateTaskCommand(String userId, String courseId, String name, String description, String code) {
		this.userId = userId;
		this.courseId = courseId;
		this.name = name;
		this.description = description;
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}

}
