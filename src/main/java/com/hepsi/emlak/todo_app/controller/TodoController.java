package com.hepsi.emlak.todo_app.controller;

import com.hepsi.emlak.todo_app.service.TodoService;
import com.hepsi.emlak.todo_app.service.dto.request.TodoRequest;
import com.hepsi.emlak.todo_app.service.dto.response.TodoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest request){
        TodoResponse response = todoService.createTodo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<Page<TodoResponse>> getUserTodos(Pageable pageable){
        Page<TodoResponse> todos = todoService.getUserTodos(pageable);
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable String id, @RequestBody TodoRequest request){
        return ResponseEntity.ok(todoService.updateTodo(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id){
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
