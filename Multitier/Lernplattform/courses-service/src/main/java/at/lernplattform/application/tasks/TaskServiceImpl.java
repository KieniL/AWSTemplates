package at.lernplattform.application.tasks;

import java.util.List;

import at.lernplattform.application.dao.CourseDao;
import at.lernplattform.application.dao.SchemaDao;
import at.lernplattform.application.dao.TaskDao;
import at.lernplattform.application.database.model.Course;
import at.lernplattform.domain.course.Feedback;
import at.lernplattform.domain.course.FeedbackFactory;
import at.lernplattform.domain.usecase.*;
import at.lernplattform.rest.api.model.SchemaModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.course.Task;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private SchemaDao schemaDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Task getTask(String courseid, String taskid) {
		return new GetTask(courseRepository, courseid, taskid).getTask();
	}

	@Override
	public List<Task> getTasks(String courseid) {
		return new GetTasks(courseRepository, courseid).getTasks();
	}

	@Override
	public Task createTask(CreateTaskCommand command) {
		return new CreateTask(courseRepository, command).createTask();
	}

	@Override
	public void deleteTask(DeleteTaskCommand command) {
		new DeleteTask(courseRepository, command).deleteTask();
	}

	@Override
	public Task updateTask(UpdateTaskCommand command) {
		return new UpdateTask(courseRepository, command).updateTask();
	}

	@Override
	public Feedback getFeedback(CreateFeedbackCommand command) {

		Course course = courseDao.getCourse(command.getCourseid());
		at.lernplattform.application.database.model.Task task = taskDao.getTask(command.getCourseid(), command.getTaskid());
		SchemaModel schema = schemaDao.getSchema(course.getSchema());

		//TODO: Validate against the DB

		return FeedbackFactory.create(GetFeedback.checkSQLStatement(task.getCode(),command.getSolved(), schema, jdbcTemplate));

	}


}
