package com.hepsi.emlak.todo_app.service.repository;

import com.hepsi.emlak.todo_app.domain.User;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface UserRepository extends CouchbaseRepository<User, String> {
    Optional<User> findByEmail(String email);
}
