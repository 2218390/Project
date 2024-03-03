package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Application;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    EventRepository eventRepository;

    public EventService() {
        super();
        // TODO Auto-generated constructor stub
    }


    public List<Event> getEvents(Long id) {
        return (List<Event>) eventRepository.findByUserId(id);
    }

    public Optional<Event> findByID(Long id) {
        return eventRepository.findById(id);
    }
    public void addEvent(Event newEvent) {
        eventRepository.save(newEvent);
    }
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        eventRepository.delete(event);
    }
}
