package at.lernplattform.application.courses;

public class CreateCourseCommand {

	private String name;
	private String creator;
	private int schema;

	public CreateCourseCommand(String name, String creator, int schema) {
		this.name = name;
		this.creator = creator;
		this.schema = schema;
	}

	public String getName() {
		return name;
	}

	public String getCreator() {
		return creator;
	}

	public int getSchema() {
		return schema;
	}

}
