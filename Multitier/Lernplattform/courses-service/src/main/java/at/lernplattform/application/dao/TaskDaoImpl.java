package at.lernplattform.application.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import at.lernplattform.application.database.model.Course;
import at.lernplattform.application.database.model.Task;
import at.lernplattform.application.rowmapper.TaskRowMapper;

@Repository
public class TaskDaoImpl implements TaskDao{

	@Autowired
    private NamedParameterJdbcTemplate template;

	@Override
	public Task getTask(String courseid, String taskid) {
		//String sql = "select * from internal.task where courseId = " + courseid + " and id = " + taskid + " ORDER BY id ASC";
		String sql = "select * FROM (select *, ROW_NUMBER () OVER (ORDER BY id) AS rownum from internal.task where courseid= " + courseid + ") s where rownum = "+ taskid +"";
		
		return template.query(sql, new TaskRowMapper()).get(0);
	}

	@Override
	public List<Task> getTasks(String courseid, Boolean deleted) {
		String sql = "select * from internal.task where courseId = " + courseid + " AND deleted = " + deleted + " ORDER BY id ASC";
		
		return template.query(sql, new TaskRowMapper());
	}

	@Override
	public Task createTask(String name, String description, String code, String courseid) {
		 final String sql = "insert into internal.task(name, description, courseId, deleted, code) values(:name,:description,:courseId, false, :code)";


	        Map<String,Object> map=new HashMap<String,Object>();  

	        map.put("name", name);
	        map.put("description", description);
	        map.put("courseId", Integer.parseInt(courseid));
	        map.put("code", code);


	        
	        template.execute(sql,map,new PreparedStatementCallback<Object>() {  

	           @Override  

	           public Object doInPreparedStatement(PreparedStatement ps)  

	                   throws SQLException, DataAccessException {  

	               return ps.executeUpdate();  

	           }  

	       });  
	        return new Task(name, description, courseid);
	}

	@Override
	public void deleteTask(String id) {
		final String sql = "update internal.task set courseId = null where id = :id";


        Map<String,Object> map=new HashMap<String,Object>();  

        map.put("id", Integer.parseInt(id));

        
        template.execute(sql,map,new PreparedStatementCallback<Object>() {  

           @Override  

           public Object doInPreparedStatement(PreparedStatement ps)  

                   throws SQLException, DataAccessException {  

               return ps.executeUpdate();  

           }  

       });  
	}


	@Override
	public Task updateTask(String id, String name, String description) {
		final String sql = "update internal.task set name = :name, description = :description where id = :id";


        Map<String,Object> map=new HashMap<String,Object>();  

        map.put("id", Integer.parseInt(id));
        map.put("name", name);
        map.put("description", description);

        
        template.execute(sql,map,new PreparedStatementCallback<Object>() {  

           @Override  

           public Object doInPreparedStatement(PreparedStatement ps)  

                   throws SQLException, DataAccessException {  

               return ps.executeUpdate();  

           }  

       }); 
        return new Task(Integer.parseInt(id), name, description);
	}
	
	

}
