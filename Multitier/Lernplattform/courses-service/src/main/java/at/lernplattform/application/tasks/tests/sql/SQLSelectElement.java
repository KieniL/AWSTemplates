package at.lernplattform.application.tasks.tests.sql;

import java.util.ArrayList;

public class SQLSelectElement {
	public String top = "";
	public boolean distinct = false;
	public ArrayList<SQLElement> selects = new ArrayList<SQLElement>();
	
	public void add(String string) {
		SQLStringElement elem = new SQLStringElement();
		elem.Value = string;
		
		selects.add(elem);
	}

	public void add(String string, String alias) {
		SQLStringElement elem = new SQLStringElement();
		elem.Value = string;
		elem.Alias = alias;
		
		selects.add(elem);		
	}
}
