package at.lernplattform.application.tasks.tests.sql;

import java.util.ArrayList;

public class SQLConditionCollection {
	public ArrayList<Object> ConditionList = new ArrayList<Object>();
	
	public SQLConditionCollection(SQLConditionElement condition) {
		ConditionList.add(condition);
	}
	
	public void addCondition(SQLConditionElement condition, String Operation) {
		ConditionList.add(Operation);
		ConditionList.add(condition);
	}
	
	public void addCondition(SQLConditionCollection condition, String Operation) {
		ConditionList.add(Operation);
		ConditionList.add(condition);
	}
}
