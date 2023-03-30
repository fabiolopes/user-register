package com.bios.user.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bios.user.dto.UserNewDTO;
import com.bios.user.dto.UserResponseDTO;
import com.bios.user.service.UserService;

@RestController
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	private UserService userService;	
	
	@PostMapping
	public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody UserNewDTO userToCreate) {
		UserResponseDTO userCreated = userService.insert(userToCreate);
		return new ResponseEntity<UserResponseDTO>(userCreated, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDTO> find(@PathVariable Long id) {
		return ResponseEntity.ok().body(userService.findById(id));
	}
	
	
}
