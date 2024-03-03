package com.example.demo.repository;

import com.example.demo.model.Application;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByEventId(Long eventId);
    List<Application> findByUserId(Long userId);
    boolean existsByUserAndEvent(User user, Event event);
}