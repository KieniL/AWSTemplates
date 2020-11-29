package at.lernplattform.application.tasks.tests;

import at.lernplattform.application.tasks.tests.sql.Token;

public class TokenReturn{
	Token tok;
	int index;
	
	public TokenReturn(Token t) {
		this.tok = t;
	}
	public TokenReturn(Token t, int i) {
		this.tok = t;
		this.index = i;
	}
}