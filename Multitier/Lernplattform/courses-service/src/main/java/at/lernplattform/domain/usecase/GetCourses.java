package at.lernplattform.domain.usecase;

import java.util.List;
import java.util.Optional;

import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;

public class GetCourses {

	private CourseRepository courseRepository;

	public GetCourses(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	public List<Course> getCourses(Optional<String> userId, boolean deleted) {
		return courseRepository.findCourses(userId, deleted);
	}

}
