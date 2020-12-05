package at.lernplattform.application.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.init.ScriptUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import at.lernplattform.application.rowmapper.CourseRowMapper;
import at.lernplattform.application.rowmapper.SchemaRowMapper;
import at.lernplattform.rest.api.model.SchemaModel;
import at.lernplattform.rest.api.model.SchemaresponseModel;

@Repository
public class SchemaDaoImpl implements SchemaDao{

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public SchemaresponseModel createSchema(SchemaModel schemaModel, String creator) {
		
		SchemaresponseModel response = new SchemaresponseModel();
		
		if(schemaModel.getName().length()==0) {
			response.setResponse("Schemaname can not be empty");
		}else
		{
			String sql = "CREATE SCHEMA " + schemaModel.getName()+ " AUTHORIZATION internaluser;";
			 
			try
			{
				jdbcTemplate.execute(sql);
				String sql2 = "insert into internal.Schema(name, creator) VALUES (:schema, :creator);";


		        Map<String,Object> map=new HashMap<String,Object>();  

		        map.put("schema", schemaModel.getName());
		        map.put("creator", Integer.parseInt(creator));


		        namedjdbcTemplate.execute(sql2,map,new PreparedStatementCallback<Object>() {  

		           @Override  

		           public Object doInPreparedStatement(PreparedStatement ps)  

		                   throws SQLException, DataAccessException {  

		               return ps.executeUpdate();  

		           }  

		       }); 
				response.setResponse(schemaModel.getName());
			}catch(Exception e)
			{
				response.setResponse(e.getMessage());
			}
		}
		
        
        
        
        return response;
	}
	
	@Override
	public List<SchemaModel> getSchemas(String userid) {
		String sql = "select * from internal.Schema where creator = " + userid;

		
		return namedjdbcTemplate.query(sql, new SchemaRowMapper());
	}
	
	@Override
	public SchemaModel getSchema(int _id) {
		String sql = "select * from internal.Schema where id = " + _id;

		
		return namedjdbcTemplate.query(sql, new SchemaRowMapper()).get(0);
	}
	
	@Override
	public SchemaresponseModel tablemanage(String schemaname, String fileContent) {
		SchemaresponseModel response = new SchemaresponseModel();
		String sql = "set schema '"+schemaname+"';" + fileContent;
		
		try
		{
			jdbcTemplate.execute(sql); 
			response.setResponse("Success");
		}catch(Exception e)
		{
			response.setResponse(e.getMessage());
		}
        
        return response;
	}

	@Override
	public SchemaresponseModel tablescreatedefault(String schemaname) {
		SchemaresponseModel response = new SchemaresponseModel();
		
	    File file = new File("src\\main\\java\\at\\lernplattform\\application\\dao\\northwind.txt");
	    InputStream inputStream;
	    StringBuilder resultStringBuilder = new StringBuilder();
		try {
			inputStream = new FileInputStream(file);
		    try (BufferedReader br
		      = new BufferedReader(new InputStreamReader(inputStream))) {
		        String line;
		        while ((line = br.readLine()) != null) {
		            resultStringBuilder.append(line).append("\n");
		        }
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = resultStringBuilder.toString();
		
		sql = sql.replaceAll("schemaname", schemaname);
		
		try
		{
			jdbcTemplate.execute(sql); 
			response.setResponse("Success");
		}catch(Exception e)
		{
			response.setResponse(e.getMessage());
		}
        
        
        return response;
	}





}
