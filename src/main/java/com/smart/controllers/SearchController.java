package com.smart.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.Dao.ContactRepository;
import com.smart.Dao.UserRepository;
import com.smart.Entities.Contact;
import com.smart.Entities.User;

@RestController
public class SearchController {
     
	
	@Autowired
	UserRepository userrepository;
	@Autowired
	ContactRepository contactrepository;
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal)
	{
		User user=this.userrepository.getUserByUserName(principal.getName());
		List<Contact>contacts=this.contactrepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	}
	
	
}
