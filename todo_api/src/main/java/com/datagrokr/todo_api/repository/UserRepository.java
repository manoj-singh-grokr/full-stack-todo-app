package com.datagrokr.todo_api.repository;

import com.datagrokr.todo_api.entity.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
