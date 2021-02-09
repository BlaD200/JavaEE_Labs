package org.vsynytsyn.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vsynytsyn.exceptions.ConstraintViolationException;
import org.vsynytsyn.exceptions.LoginExistsException;
import org.vsynytsyn.model.NewUser;
import org.vsynytsyn.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserValidator userValidator;


    @Test
    void shouldPassValidation() {
        userValidator.validateNewUser(
                NewUser.builder()
                        .login("login")
                        .password("pass")
                        .fullName("name")
                        .build()
        );

        verify(repository).isLoginExists("login");
    }


    @Test
    void shouldThrow_LoginExistsException() {
        String login = "login";
        when(repository.isLoginExists(login)).thenReturn(true);

        assertThatThrownBy(() ->
                userValidator.validateNewUser(
                        NewUser.builder().login(login).password("pass").fullName("name").build()
                )
        ).isInstanceOf(LoginExistsException.class);
    }


    @ParameterizedTest
    @ValueSource(strings = {"12", "12345678"})
    void shouldThrowsErrorWhenPasswordHasInvalidLength(final String password) {
        assertThatThrownBy(() ->
                userValidator.validateNewUser(
                        NewUser.builder().login("login").password(password).fullName("name").build()
                )
        ).isInstanceOfSatisfying(ConstraintViolationException.class,
                exception -> assertThat(exception.getErrors().contains("Password has invalid size"))
        ).hasMessage("You have errors in you object");
    }


    @Test
    void shouldThrowErrorWhenPasswordContainsInvalidSymbols() {
        assertThatThrownBy(() ->
                userValidator.validateNewUser(
                        NewUser.builder().login("login").password("bh&2&Y*&#N~(").fullName("name").build()
                )
        ).isInstanceOfSatisfying(ConstraintViolationException.class,
                exception -> assertThat(exception.getErrors().contains("Password doesn't match regex"))
        ).hasMessage("You have errors in you object");
    }


}