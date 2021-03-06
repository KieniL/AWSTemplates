package at.lernplattform.infrastructure;

import org.springframework.security.core.Authentication;

public interface TokenService {

	String generateToken(String email);

	boolean validateToken(String jwtToken);

	Authentication getAuthentication(String token);

}
