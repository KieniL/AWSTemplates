package at.lernplattform.application.tasks.tests.sql;

import java.util.ArrayList;

public class SQLFromElement {
	public ArrayList<SQLElement> Tables = new ArrayList<SQLElement>();
	public SQLConditionCollection OnClauses;
	
	public void addTable(String string) {
		SQLElement elem = new SQLElement();
		elem.Table = string;
		
		Tables.add(elem);
	}

	public void addTable(String string, String alias) {
		SQLElement elem = new SQLElement();
		elem.Table = string;
		elem.Alias = alias;
		
		Tables.add(elem);		
	}
	
	
}
