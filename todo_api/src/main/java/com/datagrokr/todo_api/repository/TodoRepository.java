package com.datagrokr.todo_api.repository;

import com.datagrokr.todo_api.entity.Todo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<Todo, String> {
    Todo findByContent(String content);
}
