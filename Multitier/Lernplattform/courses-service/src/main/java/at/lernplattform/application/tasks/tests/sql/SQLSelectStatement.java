package at.lernplattform.application.tasks.tests.sql;

import java.util.ArrayList;
import java.util.List;

public class SQLSelectStatement extends SQLElement{
	public SQLSelectElement Select;
	public SQLFromElement From;
	public SQLConditionCollection  WhereClauses;
	public ArrayList<SQLElement> GroupByList =  new ArrayList<SQLElement>();
	public SQLConditionCollection HavingConditions;
	public ArrayList<SQLElement> OrderByList =  new ArrayList<SQLElement>();

	public SQLSelectStatement() {
		Select = new SQLSelectElement();
		From = new SQLFromElement();	
	}
}