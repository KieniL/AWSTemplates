package at.lernplattform.application.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import at.lernplattform.application.database.model.Course;



public class CourseRowMapper implements RowMapper<Course> {

	@Override
	public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
		Course course = new Course(
				rs.getString("id"),
				rs.getString("name"),
				rs.getString("creator"),
				rs.getBoolean("deleted"),
				rs.getInt("schema"));

		return course;
	}

}
