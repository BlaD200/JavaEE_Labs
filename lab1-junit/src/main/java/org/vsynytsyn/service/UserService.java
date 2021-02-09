package org.vsynytsyn.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vsynytsyn.model.NewUser;
import org.vsynytsyn.model.User;
import org.vsynytsyn.repository.UserRepository;
import org.vsynytsyn.validation.UserValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    public void createNewUser(final NewUser newUser) {
        log.info("Try to create new user: {}", newUser.getLogin());
        userValidator.validateNewUser(newUser);
        final User user = userRepository.saveNewUser(newUser);
        log.info("New user is created: {}", user);
    }

    public User getUserByLogin(final String login) {
        log.info("Try to get user by login: {}", login);
        return userRepository.getUserByLogin(login);
    }

}
