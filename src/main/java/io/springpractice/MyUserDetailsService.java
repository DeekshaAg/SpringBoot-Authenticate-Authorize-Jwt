package io.springpractice;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.springpractice.model.MyUserDetails;
import io.springpractice.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return new MyUserDetails(userRepository.findByUserName(username));
				//new User("user","pass",new ArrayList<>());
	}

}
