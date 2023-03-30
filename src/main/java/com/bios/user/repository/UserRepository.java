package com.bios.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.bios.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Transactional(readOnly=true)
	User findByEmail(String email);
}
