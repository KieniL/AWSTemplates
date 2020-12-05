package at.lernplattform.application.courses;

public class CourseNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String id;

	public CourseNotFoundException(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
