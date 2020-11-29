package at.lernplattform.domain.course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {

	List<Course> findCourses(Optional<String> userId, boolean deleted);

	Course findCourse(String courseId);

	Course save(Course course);
	
	Course update(Course course);
	
	Course restore(Course course);
	
	Course createTask(Task task, String courseId);
	
	Task getTask(String courseId, String taskid);
	
	Course updateTask(Task task, String courseId);
	
	void removeTask(String taskid);

	void delete(String courseId);

}
