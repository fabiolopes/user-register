package com.bios.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bios.user.dto.UserNewDTO;
import com.bios.user.dto.UserResponseDTO;
import com.bios.user.exceptions.ObjectNotFoundException;
import com.bios.user.model.User;
import com.bios.user.repository.UserRepository;


@Service
@SuppressWarnings("unused")
public class UserService {

	@Autowired
	private UserRepository repo;
    @Autowired
    private ModelMapper modelMapper;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	public UserResponseDTO insert(UserNewDTO userNewDTO) {
		User userToCreate = modelMapper.map(userNewDTO, User.class);
		userToCreate.setId(null);
		userToCreate.setPassword(encoder.encode(userToCreate.getPassword()));
		userToCreate = repo.save(userToCreate);
		return modelMapper.map(userToCreate, UserResponseDTO.class);
	}
	
	public UserResponseDTO findById(Long id) {
		User userFound = repo.findById(id).orElseThrow(() -> new ObjectNotFoundException(
				"User not found! Id: " + id));
		return modelMapper.map(userFound, UserResponseDTO.class);
	}
	
}
