package com.datagrokr.todo_api.repository;

import java.time.LocalDateTime;

import com.datagrokr.todo_api.security.token.ConfirmationToken;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String>{
    ConfirmationToken findByToken(String token);
}
