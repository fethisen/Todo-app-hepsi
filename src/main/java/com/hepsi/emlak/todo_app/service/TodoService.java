package com.hepsi.emlak.todo_app.service;

import com.hepsi.emlak.todo_app.config.security.jwt.JwtUtils;
import com.hepsi.emlak.todo_app.domain.Todo;
import com.hepsi.emlak.todo_app.domain.User;
import com.hepsi.emlak.todo_app.service.dto.request.TodoRequest;
import com.hepsi.emlak.todo_app.service.dto.response.TodoResponse;
import com.hepsi.emlak.todo_app.service.exception.BusinessException;
import com.hepsi.emlak.todo_app.service.exception.ForbiddenException;
import com.hepsi.emlak.todo_app.service.repository.TodoRepository;
import com.hepsi.emlak.todo_app.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public TodoResponse createTodo(TodoRequest request){
        User user = getUser();
        Todo todo = new Todo();
        todo.setId(java.util.UUID.randomUUID().toString());
        todo.setUserId(user.getId());
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());
        todo = todoRepository.save(todo);
        return getTodoResponse(todo);
    }

    public Page<TodoResponse> getUserTodos(Pageable pageable){
        User user = getUser();
        Page<Todo> todos = todoRepository.findByUserId(user.getId(), pageable);
        return todos.map(this::getTodoResponse);

    }

    public TodoResponse updateTodo(String todoId, TodoRequest todoRequest){
        Todo todo = getTodoByCurrentUser(todoId);
        todo.setTitle(todoRequest.getTitle());
        todo.setDescription(todoRequest.getDescription());
        todo.setCompleted(todoRequest.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        todo = todoRepository.save(todo);
        return getTodoResponse(todo);
    }

    public void deleteTodo(String todoId){
        Todo todo = getTodoByCurrentUser(todoId);
        todoRepository.delete(todo);
    }

    private Todo getTodoByCurrentUser(String todoId){
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(()-> new BusinessException("Todo not found"));
        User user = getUser();
        if (!todo.getUserId().equals(user.getId())){
            throw new ForbiddenException("You don't have permission to update this todo.");
        }
        return todo;
    }
    private User getUser(){
        String userEmail = jwtUtils.getCurrentUserEmail();
        Optional<User> optUser = userRepository.findByEmail(userEmail);
        if (optUser.isEmpty()){
            log.error("Elde edilen email bilgisi sistemde bulunmuyor.! Email adresi: {}",userEmail);
            throw new BusinessException("Sistemsel bir hata var, lütfen sistem yöneticisi ile iletişime geçiniz.");
        }
        return optUser.get();
    }

    private  TodoResponse getTodoResponse(Todo todo) {
        return TodoResponse.builder()
                .todoId(todo.getId())
                .userId(todo.getUserId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .createdAt(todo.getCreatedAt())
                .dueDate(todo.getDueDate())
                .build();
    }
}
