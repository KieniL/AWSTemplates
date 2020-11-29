package at.lernplattform.application.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import at.lernplattform.rest.api.model.SchemaModel;




public class SchemaRowMapper implements RowMapper<SchemaModel> {

	@Override
	public SchemaModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		SchemaModel schema = new SchemaModel()
				.name(rs.getString("name")).id(rs.getString("id"));

		return schema;
	}

}
