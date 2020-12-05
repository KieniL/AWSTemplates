package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.courses.DeleteCourseCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;

public class DeleteCourse {

	private CourseRepository courseRepository;
	private DeleteCourseCommand command;

	public DeleteCourse(CourseRepository courseRepository, DeleteCourseCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public void deleteCourse() {
		Course course = courseRepository.findCourse(command.getCourseId());
		if (!course.getCreator().equals(command.getUserId())) {
			throw new CourseAccessProhibitedException();
		}
		courseRepository.delete(command.getCourseId());
	}

}
