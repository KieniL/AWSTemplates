package at.lernplattform.application.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import at.lernplattform.application.database.model.Course;
import at.lernplattform.application.rowmapper.CourseRowMapper;
import at.lernplattform.application.rowmapper.TaskRowMapper;

@Repository
public class CourseDaoImpl implements CourseDao{

	@Autowired
    private NamedParameterJdbcTemplate template;
	
	@Override
	public List<Course> getCourses(Optional<String> userId, boolean deleted) {
		
		String sql = "";
		
		if (userId.isPresent()) {
			sql = "select * from internal.course where deleted = " + deleted+ " AND creator="+ userId.get() +" ORDER BY id asc";
		}else {
			sql = "select * from internal.course where deleted = " + deleted +" ORDER BY id asc";
		}
		
		return template.query(sql, new CourseRowMapper());

	}
	
	@Override
	public Course getCourse(String courseId) {
		String sql = "";
		

		sql = "select * from internal.course where id ="+courseId;
		
		Course c = template.query(sql, new CourseRowMapper()).get(0);
	
		
		String sql2 = "select * from internal.task where courseId = " + courseId;
		
		
		c.setTasks(template.query(sql2, new TaskRowMapper()));
		
		return c;
	}

	@Override
	public Course createCourse(String name, boolean deleted, int courseowner, int schema) {
		 final String sql = "insert into internal.course(name, deleted, creator, schema) values(:name,:deleted,:creator,:schema)";


	        Map<String,Object> map=new HashMap<String,Object>();  

	        map.put("name", name);
	        map.put("deleted", deleted);
	        map.put("creator", courseowner);
	        map.put("schema", schema);


	        
	        template.execute(sql,map,new PreparedStatementCallback<Object>() {  

	           @Override  

	           public Object doInPreparedStatement(PreparedStatement ps)  

	                   throws SQLException, DataAccessException {  

	               return ps.executeUpdate();  

	           }  

	       });  
	        return new Course(name, Integer.toString(courseowner), deleted, null, schema);

	}



	@Override
	public Course updateCourse(int courseId, String name) {
		final String sql = "update internal.course set name = :name where id = :courseId";

		 Map<String,Object> map=new HashMap<String,Object>();  

		 map.put("courseId", courseId);
		 map.put("name", name);

		 template.execute(sql,map,new PreparedStatementCallback<Object>() {  

		    @Override  

		    public Object doInPreparedStatement(PreparedStatement ps)  

		            throws SQLException, DataAccessException {  

		        return ps.executeUpdate();  

		    }
		    
		});  
		 return getCourse(Integer.toString(courseId));
	}

	@Override
	public void restoreCourse(int courseId) {
		final String sql = "update internal.course set deleted = false where id =:courseId";

		 Map<String,Object> map=new HashMap<String,Object>();  

		 map.put("courseId", courseId);

		 template.execute(sql,map,new PreparedStatementCallback<Object>() {  

		    @Override  

		    public Object doInPreparedStatement(PreparedStatement ps)  

		            throws SQLException, DataAccessException {  

		        return ps.executeUpdate();  

		    }  

		});  
	}

	@Override
	public void deleteCourse(int courseId) {
		 final String sql = "update internal.course set deleted = true where id =:courseId";

		 Map<String,Object> map=new HashMap<String,Object>();  

		 map.put("courseId", courseId);

		 template.execute(sql,map,new PreparedStatementCallback<Object>() {  

		    @Override  

		    public Object doInPreparedStatement(PreparedStatement ps)  

		            throws SQLException, DataAccessException {  

		        return ps.executeUpdate();  

		    }  

		});  
		
	}

}
