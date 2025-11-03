package com.woochat.repository;

import com.woochat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Custom method to get recent messages ordered by time
    @Query("SELECT m FROM Message m ORDER BY m.createdAt DESC")
    List<Message> findAllByOrderByCreatedAtDesc();
    
    // Alternative: Use Limit annotation (Spring Data JPA 3.x)
    @Query(value = "SELECT * FROM messages ORDER BY created_at DESC LIMIT 50", nativeQuery = true)
    List<Message> findTop50ByOrderByCreatedAtDesc();
}
