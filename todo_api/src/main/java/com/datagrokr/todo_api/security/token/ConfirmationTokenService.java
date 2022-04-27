package com.datagrokr.todo_api.security.token;

import java.time.LocalDateTime;

import com.datagrokr.todo_api.repository.ConfirmationTokenRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public ConfirmationToken setConfirmedAt(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        return confirmationTokenRepository.save(confirmationToken);
    }
}
