package io.springpractice.Utilties;



import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

@Service
public class JwtUtil {
	
	private String SECRET_KEY = "secret";
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date(System.currentTimeMillis()));
	}
	public String generateToken(UserDetails userDetails) {
		HashMap<String, Object> claims  = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	private String createToken(HashMap<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*6*10))
				.signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact();
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}
