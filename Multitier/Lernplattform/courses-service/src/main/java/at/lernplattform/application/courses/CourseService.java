package at.lernplattform.application.courses;

import java.util.List;
import java.util.Optional;

import at.lernplattform.domain.course.Course;

public interface CourseService {

	List<Course> getCourses(Optional<String> userId, boolean deleted);

	Course createCourse(CreateCourseCommand command);

	Course getCourse(String courseId);

	Course updateCourse(UpdateCourseCommand command);
	
	Course restoreCourse(RestoreCourseCommand command);
	
	void deleteCourse(DeleteCourseCommand command);


}
