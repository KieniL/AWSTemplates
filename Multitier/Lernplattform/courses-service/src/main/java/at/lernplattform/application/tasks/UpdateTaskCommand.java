package at.lernplattform.application.tasks;

public class UpdateTaskCommand {

	private String userId;
	private String courseId;
	private String taskId;
	private String name;
	private String description;

	public UpdateTaskCommand(String userId, String courseid, String taskId, String name, String description) {
		this.userId = userId;
		this.courseId = courseid;
		this.taskId = taskId;
		this.name = name;
		this.description = description;
	}

	public String getUserId() {
		return userId;
	}

	public String getCourseId() {
		return courseId;
	}

	public String getTaskId() {
		return taskId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

}
