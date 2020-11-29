package at.lernplattform.application.database.model;

public class Task {

	private String id;
	private String name;
	private String description;
	private String courseId;
	private String code;
	
	public Task(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Task(int id, String name, String description) {
		this(name, description);
		this.id = Integer.toString(id);
	}
	
	public Task(String name, String description, String courseId) {
		this(name, description);
		this.courseId = courseId;
	}
	
	public Task(String id, String name, String description, String courseId) {
		this(name, description, courseId);
		this.id = id;
	}

	public Task(String id, String name, String description, String courseId, String code) {
		this(id, name, description ,courseId);
		this.code = code;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getCourseId() {
		return courseId;
	}


	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
