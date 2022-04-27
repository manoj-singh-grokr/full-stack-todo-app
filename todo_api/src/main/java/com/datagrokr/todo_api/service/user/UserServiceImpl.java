package com.datagrokr.todo_api.service.user;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.datagrokr.todo_api.entity.Todo;
import com.datagrokr.todo_api.entity.User;
import com.datagrokr.todo_api.repository.TodoRepository;
import com.datagrokr.todo_api.repository.UserRepository;
import com.datagrokr.todo_api.security.token.ConfirmationToken;
import com.datagrokr.todo_api.security.token.ConfirmationTokenService;
import com.mongodb.lang.Nullable;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final TodoRepository todoRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return user;
    }

    public String signUpUser(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public User enableUser(String email) {
        User user = userRepo.findByEmail(email);
        user.setEnabled(true);
        return userRepo.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByEmail(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    public List<Todo> addTodo(Todo todo, User user) {

        if (user != null) {
            todoRepo.save(todo);
            user.getTodoList().add(todo);
            userRepo.save(user);
            return user.getTodoList();
        }

        return Collections.emptyList();
    }

    public List<Todo> getTodos(User user) {
        return user.getTodoList();
    }

    public List<Todo> deleteTodo(String id, User user) {
        Todo todo = todoRepo.findById(id).orElse(null);
        user.getTodoList().remove(todo);
        todoRepo.delete(todo);
        userRepo.save(user);
        return getTodos(user);
    }

    public List<Todo> updateTodo(String id, String content, User user) {
        Todo todo = todoRepo.findById(id).orElse(null);
        todo.setContent(content);
        todoRepo.save(todo);
        return getTodos(user);
    }

    public List<Todo> markTodoAsComplete(String id, User user) {
        Todo todo = todoRepo.findById(id).orElse(null);
        todo.setCompleted(!todo.getCompleted());
        todoRepo.save(todo);
        return user.getTodoList();
    }

}
