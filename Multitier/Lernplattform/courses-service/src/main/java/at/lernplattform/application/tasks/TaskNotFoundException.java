package at.lernplattform.application.tasks;

public class TaskNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String id;

	public TaskNotFoundException(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
