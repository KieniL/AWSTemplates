package at.lernplattform.application.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import at.lernplattform.application.database.model.Task;

public class TaskRowMapper implements RowMapper<Task> {

	@Override
	public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
		Task task = new Task(
				rs.getString("id"),
				rs.getString("name"),
				rs.getString("description"),
				rs.getString("courseId"),
				rs.getString("code"));

		return task;
	}

}
