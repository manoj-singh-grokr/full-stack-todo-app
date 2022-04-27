package com.datagrokr.todo_api.service.user;

import java.util.List;

import com.datagrokr.todo_api.entity.Todo;
import com.datagrokr.todo_api.entity.User;

public interface UserService {
    User getUser(String username);

    List<User> getUsers();

    List<Todo> getTodos(User user);

    List<Todo> deleteTodo(String id, User user);

    List<Todo> addTodo(Todo todo, User user);
}
