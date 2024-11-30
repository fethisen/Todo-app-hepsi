package com.hepsi.emlak.todo_app.service.repository;

import com.hepsi.emlak.todo_app.domain.Todo;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoRepository extends CouchbaseRepository<Todo, String> {
    Page<Todo> findByUserId(String userId, Pageable pageable);
}
