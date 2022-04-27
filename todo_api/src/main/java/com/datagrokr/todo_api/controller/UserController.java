package com.datagrokr.todo_api.controller;

import java.util.List;

import com.datagrokr.todo_api.entity.Role;
import com.datagrokr.todo_api.entity.Todo;
import com.datagrokr.todo_api.entity.User;
import com.datagrokr.todo_api.service.user.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@AuthenticationPrincipal User user) {
        if (user.getAuthorities().contains(Role.ADMIN)) {
            return ResponseEntity.ok().body(userService.getUsers());
        }
        return null;
    }

    @GetMapping("/checklogged")
    public ResponseEntity<String> checkLogged(@AuthenticationPrincipal User user) {
        if (user != null) {
            return ResponseEntity.ok().body(user.getFirstName());
        }
        return ResponseEntity.badRequest().body("User is not logged in!");
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> showTodos(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.getTodos(user));
    }

    @PostMapping("/todos/add")
    public ResponseEntity<List<Todo>> addTodo(@RequestBody Todo todo, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.addTodo(todo, user));
    }

    @DeleteMapping("/todos/delete/{todo}")
    public ResponseEntity<List<Todo>> deleteTodo(@PathVariable("todo") String todo,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.deleteTodo(todo, user));
    }

    @PutMapping("/todos/complete/{todo}")
    public ResponseEntity<List<Todo>> markTodo(@PathVariable("todo") String todo,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.markTodoAsComplete(todo, user));
    }

    @PutMapping("/todos/update/{todo}")
    public ResponseEntity<List<Todo>> updateTodo(@PathVariable("todo") String todo_id, @RequestBody Todo todo,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(userService.updateTodo(todo_id, todo.getContent(), user));
    }

}
