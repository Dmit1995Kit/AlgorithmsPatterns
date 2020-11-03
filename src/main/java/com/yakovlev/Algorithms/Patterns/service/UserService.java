package com.yakovlev.Algorithms.Patterns.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yakovlev.Algorithms.Patterns.domain.Role;
import com.yakovlev.Algorithms.Patterns.domain.User;
import com.yakovlev.Algorithms.Patterns.repos.UserRepo;

@Service
public class UserService implements UserDetailsService{

	@Autowired
    private UserRepo userRepo;
	
	@Autowired
	private MailSender mailSender;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepo.findByUsername(username);
	}
	
	public boolean addUser(User user){
		User userFromDb = userRepo.findByUsername(user.getUsername());
		
		if(userFromDb != null) {
			return false;
		}
		
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.USER));
		user.setActivationCode(UUID.randomUUID().toString());
		
		userRepo.save(user);
		
		if(!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello, %s! \n" + 
			                  "Welcome to Sweater. Please visit next link: http://localhost:8080/activate/%s",
			                  user.getUsername(),
			                  user.getActivationCode()
					);
			mailSender.send(user.getEmail(), "Activation code", message);
		}
		
		return true;
	}
	public boolean activateUser(String code) {
		User user = userRepo.findByActivationCode(code);
		if(user == null) {
			return false; 
		}
		
		user.setActivationCode(null);
		userRepo.save(user);
		
		return true;
	}
}