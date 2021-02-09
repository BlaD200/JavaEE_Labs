package org.vsynytsyn.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.vsynytsyn.exceptions.LoginExistsException;
import org.vsynytsyn.exceptions.UserNotFoundException;
import org.vsynytsyn.model.NewUser;
import org.vsynytsyn.repository.UserRepository;
import org.vsynytsyn.validation.UserValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceITest {

    @Autowired
    private UserService userService;
    @SpyBean
    private UserRepository userRepository;
    @SpyBean
    private UserValidator userValidator;


    @Test
    void shouldCreateNewUser() {
        NewUser newUser = NewUser.builder().login("login").password("pass").fullName("name").build();
        userService.createNewUser(newUser);

        verify(userValidator).validateNewUser(newUser);
        verify(userRepository).saveNewUser(newUser);
        verify(userRepository).isLoginExists(newUser.getLogin());

        assertThat(userRepository.getUserByLogin(newUser.getLogin()))
                .hasFieldOrPropertyWithValue("login", newUser.getLogin())
                .hasFieldOrPropertyWithValue("password", newUser.getPassword())
                .hasFieldOrPropertyWithValue("fullName", newUser.getFullName());
    }


    @Test
    void shouldThrowLoginExistsException() {
        NewUser newUser = NewUser.builder().login("loginExists").password("pass").fullName("name").build();
        userRepository.saveNewUser(newUser);
        Mockito.reset(userRepository);

        assertThrows(LoginExistsException.class, () -> userService.createNewUser(newUser));

        verify(userValidator, times(1)).validateNewUser(newUser);
        verify(userRepository, times(1)).isLoginExists(newUser.getLogin());
        verifyNoMoreInteractions(userRepository);
    }


    @Test
    void shouldReturnUserByLogin() {
        NewUser newUser = NewUser.builder().login("newLogin").password("pass").fullName("name").build();
        userService.createNewUser(newUser);

        assertThat(userService.getUserByLogin(newUser.getLogin()))
                .isNotNull()
                .isEqualToComparingFieldByField(newUser);

        verify(userRepository).getUserByLogin(anyString());
    }


    @Test
    void shouldThrowUserNotFoundException() {
        assertThatThrownBy(() -> userService.getUserByLogin("notFound"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Can't find user by login: notFound");

    }


}