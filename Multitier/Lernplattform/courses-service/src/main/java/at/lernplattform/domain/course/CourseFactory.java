package at.lernplattform.domain.course;

import java.util.ArrayList;

import at.lernplattform.application.courses.CreateCourseCommand;

public final class CourseFactory {

	private CourseFactory() {
	}

	public static Course createNew(CreateCourseCommand command) {
		return new Course(null, command.getName(), command.getCreator(), false, new ArrayList<Task>(), command.getSchema());
	}

}
