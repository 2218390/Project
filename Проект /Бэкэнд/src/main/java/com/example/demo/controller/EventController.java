package com.example.demo.controller;

import com.example.demo.model.Application;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EventService eventService;

    @PostMapping(path="/create/{userId}")
    public ResponseEntity<?> createEvent(@PathVariable Long userId, @RequestBody Event event) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            event.setUser(user);
            Event savedEvent = eventRepository.save(event);
            return ResponseEntity.ok(savedEvent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok(events);
    }
    @GetMapping("/{userId}/events")
    public ResponseEntity<List<Event>> getEventsByUserId(@PathVariable Long userId) {
        List<Event> events = eventService.getEvents(userId);
        return ResponseEntity.ok(events);
    }
    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable(value = "id") long Id) {
        eventService.deleteEvent(Id);
        return "User Deleted";
    }
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEventsByName(@RequestParam String name) {
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/city")
    public ResponseEntity<List<Event>> getEventsByLocation(@RequestParam String location) {
        List<Event> events = eventRepository.findByLocation(location);
        return ResponseEntity.ok(events);
    }
}
