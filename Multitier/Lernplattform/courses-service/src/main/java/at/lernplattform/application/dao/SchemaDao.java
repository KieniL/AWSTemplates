package at.lernplattform.application.dao;

import java.util.List;

import at.lernplattform.rest.api.model.SchemaModel;
import at.lernplattform.rest.api.model.SchemaresponseModel;

public interface SchemaDao {

	SchemaresponseModel createSchema(SchemaModel schemaModel, String creator);
	
	SchemaresponseModel tablescreatedefault(String schemaname);
	
	List<SchemaModel> getSchemas(String userid);
	
	SchemaModel getSchema(int _id);
	
	SchemaresponseModel tablemanage(String schemaname, String fileContent);
}
