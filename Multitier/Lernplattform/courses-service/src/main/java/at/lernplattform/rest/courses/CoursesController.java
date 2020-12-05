package at.lernplattform.rest.courses;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import at.lernplattform.application.tasks.*;
import at.lernplattform.domain.course.Feedback;
import at.lernplattform.rest.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.lernplattform.application.courses.CourseService;
import at.lernplattform.application.courses.CreateCourseCommand;
import at.lernplattform.application.courses.DeleteCourseCommand;
import at.lernplattform.application.courses.RestoreCourseCommand;
import at.lernplattform.application.courses.UpdateCourseCommand;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.Task;
import at.lernplattform.rest.SecurityContext;
import at.lernplattform.rest.api.CoursesApi;

@RestController
public class CoursesController implements CoursesApi {

	@Autowired
	private CourseService courseService;

	@Autowired
	private TaskService taskService;

	@Override
	public ResponseEntity<List<CourseOverviewModel>> getCourses(
			@RequestParam(value = "userid", required = false) String userid,
			@RequestParam(value = "deleted", required = false, defaultValue = "false") Boolean deleted) {
		List<Course> courses = courseService.getCourses(Optional.ofNullable(userid), deleted);
		List<CourseOverviewModel> response = courses.stream().map(CourseOverviewAdapter::new)
				.map(CourseOverviewAdapter::createJson).collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}
	
	

	@Override
	public ResponseEntity<CourseModel> postCourses(@Valid @RequestBody CourseOverviewModel courseModel) {
		CreateCourseCommand command = new CreateCourseCommand(courseModel.getName(),
				SecurityContext.getAuthenticatedUser(), courseModel.getSchema());
		Course course = courseService.createCourse(command);
		CourseModel response = new CourseAdapter(course).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CourseModel> getCourse(@PathVariable("courseid") String courseid) {
		Course course = courseService.getCourse(courseid);
		CourseModel response = new CourseAdapter(course).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CourseModel> putCourse(@PathVariable("courseid") String courseid,
			@Valid @RequestBody CourseModel courseModel) {
		UpdateCourseCommand command = new UpdateCourseCommand(courseid, courseModel.getName(),
				SecurityContext.getAuthenticatedUser());
		Course course = courseService.updateCourse(command);
		CourseModel response = new CourseAdapter(course).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<FeedbackResponseModel> putFeedback(@PathVariable("courseid") String courseid,
			@PathVariable("taskid") String taskid, @RequestBody FeedbackRequestModel feedbackRequestModel) {
		CreateFeedbackCommand command = new CreateFeedbackCommand(feedbackRequestModel.getTaskid(),
				feedbackRequestModel.getCourseid(), feedbackRequestModel.getSolved());
		Feedback feedbackForReturn = taskService.getFeedback(command);
		FeedbackResponseModel response = new FeedbackAdapter(feedbackForReturn).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> deleteCourse(@PathVariable("courseid") String courseid) {
		DeleteCourseCommand command = new DeleteCourseCommand(SecurityContext.getAuthenticatedUser(), courseid);
		courseService.deleteCourse(command);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<TaskModel>> getTasks(@PathVariable("courseid") String courseid) {
		List<Task> tasks = taskService.getTasks(courseid);
		List<TaskModel> response = tasks.stream().map(TaskAdapter::new).map(TaskAdapter::createJson)
				.collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TaskModel> postTask(@PathVariable("courseid") String courseid,
			@Valid @RequestBody TaskModel taskModel) {
		CreateTaskCommand command = new CreateTaskCommand(SecurityContext.getAuthenticatedUser(), courseid,
				taskModel.getName(), taskModel.getDescription(), taskModel.getCode());
		Task task = taskService.createTask(command);
		TaskModel response = new TaskAdapter(task).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TaskModel> getTask(@PathVariable("courseid") String courseid,
			@PathVariable("taskid") String taskid) {
		Task task = taskService.getTask(courseid, taskid);
		TaskModel response = new TaskAdapter(task).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<TaskModel> putTask(@PathVariable("courseid") String courseid,
			@PathVariable("taskid") String taskid, @Valid @RequestBody TaskModel taskModel) {
		UpdateTaskCommand command = new UpdateTaskCommand(SecurityContext.getAuthenticatedUser(), courseid, taskid,
				taskModel.getName(), taskModel.getDescription());
		Task task = taskService.updateTask(command);
		TaskModel response = new TaskAdapter(task).createJson();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> deleteTask(@PathVariable("courseid") String courseid,
			@PathVariable("taskid") String taskid) {
		DeleteTaskCommand command = new DeleteTaskCommand(SecurityContext.getAuthenticatedUser(), courseid, taskid);
		taskService.deleteTask(command);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Void> restoreCourse(@PathVariable("courseid") String courseid) {
		RestoreCourseCommand command = new RestoreCourseCommand(courseid, SecurityContext.getAuthenticatedUser());
		courseService.restoreCourse(command);
		return new ResponseEntity<>(HttpStatus.OK);
	}






}