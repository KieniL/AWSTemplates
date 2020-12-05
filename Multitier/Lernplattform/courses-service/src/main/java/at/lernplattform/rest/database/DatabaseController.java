package at.lernplattform.rest.database;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import at.lernplattform.application.dao.SchemaDao;
import at.lernplattform.rest.SecurityContext;
import at.lernplattform.rest.api.DatabaseApi;
import at.lernplattform.rest.api.model.CourseOverviewModel;
import at.lernplattform.rest.api.model.FileContentModel;
import at.lernplattform.rest.api.model.SchemaModel;
import at.lernplattform.rest.api.model.SchemaresponseModel;




@RestController
public class DatabaseController implements DatabaseApi {

	@Autowired
	private SchemaDao schemaDao;
	
	@Override
	public ResponseEntity<SchemaresponseModel> schemacreate(@Valid @RequestBody SchemaModel schemaModel) {
		return ResponseEntity.ok(schemaDao.createSchema(schemaModel, SecurityContext.getAuthenticatedUser()));
	}

	@Override
	public ResponseEntity<SchemaresponseModel> tablescreatedefault(@PathVariable("schemaname") String schemaname) {
		SchemaresponseModel response = schemaDao.tablescreatedefault(schemaname);
		
	            
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<List<SchemaModel>> getSchemas(@RequestParam(value = "userid", required = true) String userid) {
		return ResponseEntity.ok(schemaDao.getSchemas(userid));
	}


	@Override
	public ResponseEntity<SchemaresponseModel> tablemanage(@PathVariable("schemaname") String schemaname, @Valid @RequestBody FileContentModel fileContentModel) {
		return ResponseEntity.ok(schemaDao.tablemanage(schemaname, fileContentModel.getContent()));
	}



}
