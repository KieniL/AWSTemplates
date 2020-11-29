package at.lernplattform.application.tasks.tests.sql;

public class SQLStringElement extends SQLElement {	
	public String Value = "";
	
	public boolean equals(SQLStringElement comp) {
		return this.Table.equals(comp.Table) &&
				this.Element.equals(comp.Element) &&
				this.Function.equals(comp.Function) &&
				this.Value.equals(comp.Value);
	}
}
