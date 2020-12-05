package at.lernplattform.rest.courses;

import java.util.List;
import java.util.stream.Collectors;

import at.lernplattform.domain.course.Course;
import at.lernplattform.rest.api.model.CourseModel;
import at.lernplattform.rest.api.model.TaskModel;

public class CourseAdapter {

	private Course course;

	public CourseAdapter(Course course) {
		this.course = course;
	}

	public CourseModel createJson() {
		List<TaskModel> tasks = course.getTasks().stream().map(TaskAdapter::new).map(TaskAdapter::createJson)
				.collect(Collectors.toList());
		return new CourseModel().id(course.getId()).name(course.getName()).tasks(tasks).deleted(course.getDeleted()).schema(course.getSchema());
	}

}
