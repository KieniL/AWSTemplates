package at.lernplattform.application.tasks.tests.sql;

public class SQLConditionElement extends SQLElement {	
	public Object Comperator1 = "";
	public String Operation = "";
	public Object Comperator2 = "";
	
	public boolean euqals(SQLConditionElement elem) {
		return Comperator1.equals(elem.Comperator1) &&
				Comperator2.equals(elem.Comperator2) &&
				Operation.equals(elem.Operation);
	}
	
	public String toString() {
		return String.format("%s %s %s", Comperator1.toString(), Operation, Comperator2.toString());
	}
}
