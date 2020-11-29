package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.courses.UpdateCourseCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;

public class UpdateCourse {

	private CourseRepository courseRepository;
	private UpdateCourseCommand command;

	public UpdateCourse(CourseRepository courseRepository, UpdateCourseCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public Course updateCourse() {
		Course course = courseRepository.findCourse(command.getCourseId());
		if (!course.getCreator().equals(command.getUser())) {
			throw new CourseAccessProhibitedException();
		}
		course.update(command);
		//return courseRepository.save(course);
		return courseRepository.update(course);
	}

}
