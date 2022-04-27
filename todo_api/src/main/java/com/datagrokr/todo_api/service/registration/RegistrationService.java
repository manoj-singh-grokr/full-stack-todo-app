package com.datagrokr.todo_api.service.registration;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.datagrokr.todo_api.entity.Role;
import com.datagrokr.todo_api.entity.User;
import com.datagrokr.todo_api.security.token.ConfirmationToken;
import com.datagrokr.todo_api.security.token.ConfirmationTokenService;
import com.datagrokr.todo_api.service.EmailValidator;
import com.datagrokr.todo_api.service.user.UserServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private UserServiceImpl userService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        return userService.signUpUser(new User(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPassword(), Role.USER, new ArrayList<>()));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token);
        if (confirmationToken == null) {
            throw new IllegalStateException("token not found");
        }

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

}
