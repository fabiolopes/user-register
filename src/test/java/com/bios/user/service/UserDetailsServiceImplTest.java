package com.bios.user.service;

import com.bios.user.dto.UserNewDTO;
import com.bios.user.dto.UserResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @SuppressWarnings("unused")
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Test
    @DisplayName("Load a existent user to log in")
    public void loadUserByUsernameSuccessful() {
        UserNewDTO userNewDTO = UserNewDTO.builder().email("teste@email.com").name("teste1").password("123456").build();
        UserResponseDTO userCreated = userService.insert(userNewDTO);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userCreated.getEmail());
        Assertions.assertTrue(userDetails.getUsername().equals(userNewDTO.getEmail()));
        Assertions.assertTrue(encoder.matches(userNewDTO.getPassword(), userDetails.getPassword()));
    }

    @Test
    @DisplayName("Throw UsernameNotFoundException after trying load inexistent user")
    public void throwUsernameNotFoundExceptionLoadUserByUsernameInexistent() {
        UserNewDTO userNewDTO = UserNewDTO.builder().email("teste@email.com").name("teste1").password("123456").build();
        Assertions.assertThrows(UsernameNotFoundException.class,
                ()-> userDetailsService.loadUserByUsername(userNewDTO.getEmail()));
    }

}