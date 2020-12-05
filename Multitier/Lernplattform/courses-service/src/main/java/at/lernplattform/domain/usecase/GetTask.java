package at.lernplattform.domain.usecase;

import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.course.Task;

public class GetTask {

	private CourseRepository courseRepository;
	private String courseId;
	private String taskId;

	public GetTask(CourseRepository courseRepository, String courseId, String taskId) {
		this.courseRepository = courseRepository;
		this.courseId = courseId;
		this.taskId = taskId;
	}

	public Task getTask() {
		Task task = courseRepository.getTask(courseId, taskId);
		setPreviousNextTaskIds(task, courseRepository.findCourse(courseId));
		return task;
	}

	private void setPreviousNextTaskIds(Task task, Course course) {
		try {
			for (int i = 0; i < course.getTasks().size(); i++) {
				if (course.getTasks().get(i).getId().equals(task.getId())) {
					if (i - 1 >= 0) {
						task.setPreviousTask("" + (i));
					}
					if (i + 1 < course.getTasks().size()) {
						task.setNextTask("" + (i + 2));
					}
					return;
				}
			}
		} catch (Exception e) {
			// ok
		}
	}

}
