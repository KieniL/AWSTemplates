package at.lernplattform.infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import at.lernplattform.application.courses.CourseNotFoundException;
import at.lernplattform.application.courses.CourseService;
import at.lernplattform.application.dao.CourseDaoImpl;
import at.lernplattform.application.dao.TaskDaoImpl;
import at.lernplattform.domain.course.Course;
import at.lernplattform.domain.course.CourseRepository;
import at.lernplattform.domain.course.Task;

@Service
//Dummy Implementierung in Memory
public class CourseRepositoryDummy implements CourseRepository {

	private List<Course> courses = new ArrayList<>();
	
	@Autowired
	@Qualifier("courseDaoImpl")
	private CourseDaoImpl coursedao;
	
	
	@Autowired
	@Qualifier("taskDaoImpl")
	private TaskDaoImpl taskdao;
	
	private List<at.lernplattform.application.database.model.Course> courses2 = new ArrayList<>();

	/*
	@PostConstruct
	private void initalizeDummyData() {
		Task course1Task1 = new Task("1", "header1", "description1");
		Task course1Task2 = new Task("2", "header2", "description2");
		Task course1Task3 = new Task("3", "header3", "description3");
		List<Task> tasks1 = new ArrayList<>();
		tasks1.add(course1Task1);
		tasks1.add(course1Task2);
		tasks1.add(course1Task3);
		Course course1 = new Course("1", "SQL where", "1", false, tasks1);
		courses.add(course1);

		Task course2Task1 = new Task("1", "header1", "description1");
		Task course2Task2 = new Task("2", "header2", "description2");
		Task course2Task3 = new Task("3", "header3", "description3");
		List<Task> tasks2 = new ArrayList<>();
		tasks2.add(course2Task1);
		tasks2.add(course2Task2);
		tasks2.add(course2Task3);
		Course course2 = new Course("2", "SQL join", "2", false, tasks2);
		courses.add(course2);
	}*/

	@Override
	public List<Course> findCourses(Optional<String> userId, boolean deleted) {
		
		courses2 = coursedao.getCourses(userId, deleted);
		
		courses = new ArrayList<>();
		for (at.lernplattform.application.database.model.Course c : courses2)
		{
			courses.add(new Course(c.getId(), c.getName(), c.getCreator(), c.getDeleted(), null,c.getSchema()));
		}
		
		return courses;
		/*
		if (userId.isPresent()) {
			return courses.stream().filter(item -> item.getCreator().equals(userId.get()) && item.getDeleted().equals(deleted)).collect(Collectors.toList());
		}
		return courses.stream().filter(item ->  item.getDeleted().equals(deleted)).collect(Collectors.toList());
		*/
		
	}

	@Override
	public Course findCourse(String courseId) {
		
		at.lernplattform.application.database.model.Course course = coursedao.getCourse(courseId);
		
		List<Task> tasks = new ArrayList<>();
		List<at.lernplattform.application.database.model.Task> tasks2 = course.getTasks();
		
		for (at.lernplattform.application.database.model.Task t : tasks2)
		{
			tasks.add(new Task(t.getId(), t.getName(), t.getDescription(), t.getCode()));
		}
		return new Course(course.getId(), course.getName(), course.getCreator(), course.getDeleted(), tasks, course.getSchema());
		/*
		return courses.stream().filter(findById(courseId)).findFirst()
				.orElseThrow(() -> new CourseNotFoundException(courseId));
				*/
	}

	private Predicate<Course> findById(String courseId) {
		return item -> item.getId().equals(courseId);
	}

	@Override
	public Course save(Course course) {
		if(course.getName().length()== 0) {
			return null;
		}else
		{
			at.lernplattform.application.database.model.Course c =
					coursedao.createCourse(course.getName(), course.getDeleted(), Integer.parseInt(course.getCreator()), course.getSchema());
			
			return new Course(c.getId(), c.getName(), c.getCreator(), c.getDeleted(), new ArrayList<Task>(), c.getSchema());
		}

		/*
		if (course.getId() == null) {
			course.setId(nextCourseId());
			courses.add(course);
		} else {
			Course current = courses.stream().filter(findById(course.getId())).findFirst()
					.orElseThrow(() -> new CourseNotFoundException(course.getId()));
			Collections.replaceAll(courses, current, course);
		}
		course.getTasks().stream().filter(task -> task.getId() == null)
				.forEach(task -> task.setId(nextTaskId(course.getTasks())));
		return course;
		*/
	}

	
	/*
	private String nextCourseId() {
		Long next = courses.stream().map(Course::getId).mapToLong(Long::valueOf).max().orElseGet(() -> 0);
		return "" + ++next;
	}

	private String nextTaskId(List<Task> tasks) {
		Long next = tasks.stream().map(Task::getId).filter(item -> item != null).mapToLong(Long::valueOf).max()
				.orElseGet(() -> 0);
		return "" + ++next;
	}
	*/

	@Override
	public void delete(String courseId) {
		coursedao.deleteCourse(Integer.parseInt(courseId));
		//courses.removeIf(item -> item.getId().equals(courseId));
		//courses.stream().filter(item -> item.getId().equals(courseId));
		/*
		for ( Course c : courses)
		{
			if(c.getId().equals(courseId))
			{
				c.setDeleted(true);
			}
		}
		*/
	}

	@Override
	public Course update(Course course) {
		return findCourse(coursedao.updateCourse(Integer.parseInt(course.getId()), course.getName()).getId());
	}

	@Override
	public Course restore(Course course) {
		coursedao.restoreCourse(Integer.parseInt(course.getId()));
		return null;
		
	}


	@Override
	public Course createTask(Task task, String courseId) {
		taskdao.createTask(task.getName(), task.getDescription(), task.getCode(), courseId);
		return findCourse(courseId);
	}

	@Override
	public Course updateTask(Task task, String courseId) {
		taskdao.updateTask(task.getId(), task.getName(), task.getDescription());
		return findCourse(courseId);
	}

	@Override
	public void removeTask(String taskid) {
		taskdao.deleteTask(taskid);
	}

	@Override
	public Task getTask(String courseId, String taskid) {
		at.lernplattform.application.database.model.Task t = taskdao.getTask(courseId, taskid);
		return new Task(t.getId(), t.getName(), t.getDescription(), t.getCode());
	}
	

}
