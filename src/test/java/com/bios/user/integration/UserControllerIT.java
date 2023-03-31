package com.bios.user.integration;

import com.bios.user.controller.exceptions.ValidationError;
import com.bios.user.dto.UserNewDTO;
import com.bios.user.dto.UserResponseDTO;
import com.bios.user.security.JWTUtil;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIT {

    private static final String ENDPOINT_USER = "/user";
    private static final String ENDPOINT_LOGIN = "/login";
    private static final String HOST_PREFIX = "http://localhost:";
    private static final String VALIDATION_MESSAGE_NAME_LENGTH = "name length must be between 2 to 120 characters";
    private static final String VALIDATION_MESSAGE_NAME_EMPTY = "name is Required";
    private static final String VALIDATION_MESSAGE_EMAIL_EMPTY = "email is required";
    private static final String VALIDATION_MESSAGE_INVALID_EMAIL = "Invalid Email";
    private static final String VALIDATION_MESSAGE_PASSWORD_EMPTY = "password is required";

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JWTUtil jwtUtil;

    @TestConfiguration
    @Lazy
    static class Config {

        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri(HOST_PREFIX + port);
            return new TestRestTemplate(restTemplateBuilder);
        }
    }


    @Test
    @DisplayName("Post returns new user when successful")
    void insertUserWhenSuccessful() {
        ResponseEntity<UserResponseDTO> userResponseEntity =
                getNewUserResponseEntity("teste1@email.com", "teste1", "123456", UserResponseDTO.class);

        Assertions.assertThat(userResponseEntity).isNotNull();
        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(userResponseEntity.getBody().getId()).isNotNull();

    }

    @Test
    @DisplayName("Insert return bad request for validation error by name empty")
    void insertWithoutEmailReturnBadRequest() {
        ResponseEntity<ValidationError> userResponseEntity =
                getNewUserResponseEntity("teste1@email", "", "123456", ValidationError.class);

        Assertions.assertThat(userResponseEntity).isNotNull();
        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(userResponseEntity.getBody().getErrors()
                .stream().findFirst().get().getMessage().equals(VALIDATION_MESSAGE_NAME_EMPTY));

    }

    @Test
    @DisplayName("Insert return bad request for validation error by name length")
    void insertReturnBadRequestValidationErrorByNameLength() {
        ResponseEntity<ValidationError> userResponseEntity =
                getNewUserResponseEntity("teste1@email.com", "t", "123456", ValidationError.class);

        Assertions.assertThat(userResponseEntity).isNotNull();
        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(userResponseEntity.getBody().getErrors()
                .stream().findFirst().get().getMessage().equals(VALIDATION_MESSAGE_NAME_LENGTH));

    }

    @Test
    @DisplayName("Insert return bad request for validation error by email empty")
    void insertReturnBadRequestValidationErrorByEmailEmpty() {
        ResponseEntity<ValidationError> userResponseEntity =
                getNewUserResponseEntity("", "teste1", "123456", ValidationError.class);

        Assertions.assertThat(userResponseEntity).isNotNull();
        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(userResponseEntity.getBody().getErrors()
                .stream().findFirst().get().getMessage().equals(VALIDATION_MESSAGE_EMAIL_EMPTY));

    }

    @Test
    @DisplayName("Insert return bad request for validation error by invalid email")
    void insertReturnBadRequestValidationErrorByInvalidEmail() {
        ResponseEntity<ValidationError> userResponseEntity =
                getNewUserResponseEntity("teste1", "teste1", "123456", ValidationError.class);

        Assertions.assertThat(userResponseEntity).isNotNull();
        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(userResponseEntity.getBody().getErrors()
                .stream().findFirst().get().getMessage().equals(VALIDATION_MESSAGE_INVALID_EMAIL));

    }

    @Test
    @DisplayName("Insert return bad request for validation error by password empty")
    void insertReturnBadRequestValidationErrorByPasswordEmpty() {
        ResponseEntity<ValidationError> userResponseEntity =
                getNewUserResponseEntity("teste1@email.com", "teste1", "", ValidationError.class);

        Assertions.assertThat(userResponseEntity).isNotNull();
        Assertions.assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(userResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(userResponseEntity.getBody().getErrors()
                .stream().findFirst().get().getMessage().equals(VALIDATION_MESSAGE_PASSWORD_EMPTY));

    }

    @Test
    @DisplayName("log in with existent user")
    void loginSuccessful() {
        String email = "teste1@email.com";
        ResponseEntity loginResponseEntity = loginUser(email);
        Optional<String> authorizationOptional = loginResponseEntity.getHeaders().get("Authorization").stream().findFirst();
        Assert.assertTrue(authorizationOptional.isPresent());
        String token = authorizationOptional.get().substring(7);
        Assert.assertTrue(jwtUtil.tokenValido(token));

    }

    @Test
    @DisplayName("get user by id")
    void getUserByIdSuccessful() {
        String password = "123456";
        String email = "teste1@email.com";
        String name = "teste1";
        ResponseEntity<UserResponseDTO> userResponseEntity =
                getNewUserResponseEntity(email, "teste1", password, UserResponseDTO.class);
        UserResponseDTO userBody = userResponseEntity.getBody();
        ResponseEntity loginResponseEntity = testRestTemplate.postForEntity(ENDPOINT_LOGIN,
                Map.of("email", userBody.getEmail(), "password", password), Object.class);
        Optional<String> authorizationOptional = loginResponseEntity.getHeaders().get("Authorization").stream().findFirst();
        Assert.assertTrue(authorizationOptional.isPresent());
        String authorization = authorizationOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);
        ResponseEntity<UserResponseDTO> responseEntityFindById = testRestTemplate.exchange(
                ENDPOINT_USER+ "/{id}", HttpMethod.GET, new HttpEntity<>(headers),  UserResponseDTO.class,
                userResponseEntity.getBody().getId());
        Assert.assertTrue(responseEntityFindById.getBody().getName().equals(name));
    }

    @Test
    @DisplayName("get user by id failed because user not found")
    void getUserByIdFailedUserNotFound() {
        String password = "123456";
        String email = "teste1@email.com";
        String name = "teste1";
        ResponseEntity<UserResponseDTO> userResponseEntity =
                getNewUserResponseEntity(email, "teste1", password, UserResponseDTO.class);
        UserResponseDTO userBody = userResponseEntity.getBody();
        ResponseEntity loginResponseEntity = testRestTemplate.postForEntity(ENDPOINT_LOGIN,
                Map.of("email", userBody.getEmail(), "password", password), Object.class);
        Optional<String> authorizationOptional = loginResponseEntity.getHeaders().get("Authorization").stream().findFirst();
        Assert.assertTrue(authorizationOptional.isPresent());
        String authorization = authorizationOptional.get();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);
        ResponseEntity<Object> responseEntityFindById = testRestTemplate.exchange(
                ENDPOINT_USER+ "/{id}", HttpMethod.GET, new HttpEntity<>(headers),  Object.class,
                10L);
        Assert.assertTrue(responseEntityFindById.getStatusCode() == HttpStatus.NOT_FOUND);
    }
    private ResponseEntity loginUser(String email){
        String password = "123456";
        ResponseEntity<UserResponseDTO> userResponseEntity =
                getNewUserResponseEntity(email, "teste1", password, UserResponseDTO.class);
        UserResponseDTO userBody = userResponseEntity.getBody();
        return testRestTemplate.postForEntity(ENDPOINT_LOGIN, Map.of("email", userBody.getEmail(), "password", password), Object.class);
    }

    private ResponseEntity getNewUserResponseEntity(String email, String name, String password, Class entity){
        UserNewDTO userNewDTO = UserNewDTO.builder().email(email).name(name).password(password).build();
        return testRestTemplate.postForEntity(ENDPOINT_USER, userNewDTO, entity);
    }
}
