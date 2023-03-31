package com.bios.user.service;

import com.bios.user.dto.UserNewDTO;
import com.bios.user.dto.UserResponseDTO;
import com.bios.user.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    @SuppressWarnings("unused")
    private UserService userService;

    @Test
    void insertSuccessful() {
        Assertions.assertNotNull(newUserResponseDTO().getId());
    }

    @Test
    void findByIdReturnUser() {
        UserResponseDTO userResponseDTO = newUserResponseDTO();
        UserResponseDTO userFound = userService.findById(userResponseDTO.getId());
        Assertions.assertNotNull(userFound);
    }

    @Test
    void findByIdThrowObjectNotFoundException() {
        ObjectNotFoundException objectNotFoundException = Assertions.assertThrows(ObjectNotFoundException.class, ()-> userService.findById(1L));
        Assertions.assertEquals("User not found! Id: 1", objectNotFoundException.getMessage());
    }

    private UserResponseDTO newUserResponseDTO(){
        UserNewDTO userNewDTO = UserNewDTO.builder().email("teste@email.com").name("teste1").password("123456").build();
        return userService.insert(userNewDTO);
    }
}