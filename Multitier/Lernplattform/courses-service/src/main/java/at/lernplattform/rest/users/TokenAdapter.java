package at.lernplattform.rest.users;

import at.lernplattform.rest.api.model.TokenModel;

public class TokenAdapter {

	private String token;
	private String userid;

	public TokenAdapter(String[] userCred) {
		this.token = userCred[0];
		this.userid = userCred[1];
	}

	public TokenModel createJson() {
		return new TokenModel().token(token).userid(userid);
	}

}
