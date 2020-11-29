package at.lernplattform.application.tasks.tests.sql;

public class SQLElement {
	public String Table = "";
	public String Element = "";
	public String Alias = "";
	public String Function = "";
	
	public boolean equals(SQLElement comp) {
		return this.Table.equals(comp.Table) &&
				this.Element.equals(comp.Element) &&
				this.Function.equals(comp.Function);
	}
}
