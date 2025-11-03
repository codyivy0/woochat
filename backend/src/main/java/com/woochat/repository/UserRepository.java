package com.woochat.repository;

import com.woochat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // JpaRepository gives: save(), findById(), findAll(), delete(), etc.

}