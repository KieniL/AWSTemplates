package at.lernplattform.application.tasks;

public class DeleteTaskCommand {

	private String userId;
	private String courseId;
	private String taskId;

	public DeleteTaskCommand(String userId, String courseId, String taskId) {
		this.userId = userId;
		this.courseId = courseId;
		this.taskId = taskId;
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

}
