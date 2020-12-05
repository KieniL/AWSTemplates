package at.lernplattform.domain.usecase;

import at.lernplattform.application.courses.CourseAccessProhibitedException;
import at.lernplattform.application.courses.RestoreCourseCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;

public class RestoreCourse {

	private CourseRepository courseRepository;
	private RestoreCourseCommand command;

	public RestoreCourse(CourseRepository courseRepository, RestoreCourseCommand command) {
		this.courseRepository = courseRepository;
		this.command = command;
	}

	public Course restoreCourse() {
		Course course = courseRepository.findCourse(command.getCourseId());
		if (!course.getCreator().equals(command.getUser())) {
			throw new CourseAccessProhibitedException();
		}
		//course.restore(command);
		//return courseRepository.save(course);
		return courseRepository.restore(course);
	}

}
