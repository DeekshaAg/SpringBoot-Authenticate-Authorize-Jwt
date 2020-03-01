package io.springpractice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.springpractice.Utilties.JwtUtil;
import io.springpractice.model.AuthenticationRequest;
import io.springpractice.model.AuthenticationResponse;

@RestController
public class HomeResource {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	
	@RequestMapping("/home")
	public String getHomepage() {
		return "HELLO !!!";
	}
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
		}
		catch(BadCredentialsException ex) {
			throw new Exception("Incorrect username or password", ex);
		}
		// the control will come to this part of the code only when a user was able to authenticate successfully.
		// now at this point we will generate the jwt token that we want to return from this /authenticate API for the client to store for future requests.
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwtToken = jwtUtil.generateToken(userDetails); 
		
		
		return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
	}
}
