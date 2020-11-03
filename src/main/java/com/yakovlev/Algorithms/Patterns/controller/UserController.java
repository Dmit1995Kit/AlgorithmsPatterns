	package com.yakovlev.Algorithms.Patterns.controller;



import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yakovlev.Algorithms.Patterns.domain.Role;
import com.yakovlev.Algorithms.Patterns.domain.User;
import com.yakovlev.Algorithms.Patterns.repos.UserRepo;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping
	public String userList(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "userList";
	}
	
	@GetMapping("{user}")
	public String userEditForm(@PathVariable User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("roles", Role.values());
		return "userEdit";
	}
	@PostMapping
	public String userSave(
			@RequestParam String username,
			@RequestParam Map<String,String> form,
			@RequestParam("userId") User user
			
		) {
		user.setUsername(username);
		
		Set<String> roles = Arrays.stream(Role.values())
				.map(Role::name)
				.collect(Collectors.toSet());
		
		user.getRoles().clear(); 
		
		for(String key : form.keySet()) {
			if(roles.contains(key)) {
				user.getRoles().add(Role.valueOf(key)); 
			}
		}
		
		userRepo.save(user);
		
		return "redirect:/user";
	}
}
