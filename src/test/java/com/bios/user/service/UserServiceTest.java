package com.bios.user.service;

import com.bios.user.dto.UserNewDTO;
import com.bios.user.dto.UserResponseDTO;
import com.bios.user.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    @SuppressWarnings("unused")
    private UserService userService;

    @Test
    @DisplayName("Create a new user when successful")
    void insertSuccessful() {
        Assertions.assertNotNull(newUserResponseDTO("teste@email.com", "teste1", "123456").getId());
    }

    @Test
    @DisplayName("Throw DataIntegrityViolationException when try create new user with existent email")
    void insertDataIntegrity() {
        newUserResponseDTO("teste@email.com", "teste1", "123456");
        newUserResponseDTO("teste2@email.com", "teste2", "123456");
        newUserResponseDTO("teste3@email.com", "teste3", "123456");
        Assertions.assertThrows(DataIntegrityViolationException.class,
                ()-> newUserResponseDTO("teste@email.com", "teste1", "123456"));
    }

    @Test
    @DisplayName("Existent user found by id")
    void findByIdReturnUser() {
        UserResponseDTO userResponseDTO = newUserResponseDTO("teste@email.com", "teste1", "123456");
        UserResponseDTO userFound = userService.findById(userResponseDTO.getId());
        Assertions.assertNotNull(userFound);
    }

    @Test
    @DisplayName("Throw ObjectNotFoundException when user id not found")
    void findByIdThrowObjectNotFoundException() {
        ObjectNotFoundException objectNotFoundException = Assertions.assertThrows(ObjectNotFoundException.class, ()-> userService.findById(1L));
        Assertions.assertEquals("User not found! Id: 1", objectNotFoundException.getMessage());
    }

    private UserResponseDTO newUserResponseDTO(String email, String name, String password){
        UserNewDTO userNewDTO = UserNewDTO.builder().email(email).name(name).password(password).build();
        return userService.insert(userNewDTO);
    }
}