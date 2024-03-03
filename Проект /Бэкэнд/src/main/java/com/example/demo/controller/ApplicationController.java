package com.example.demo.controller;

import com.example.demo.model.Application;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply/{userId}/{eventId}")
    public ResponseEntity<?> applyForEvent(@PathVariable Long eventId, @PathVariable Long userId, @RequestBody Application application) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalEvent.isPresent() && optionalUser.isPresent()) {
            Event event = optionalEvent.get();
            User user = optionalUser.get();
            if (event.getUser().getId().equals(user.getId())) {
                return ResponseEntity.badRequest().body("Cannot apply for your own event.");
            }
            if (event.getFull()) {
                return ResponseEntity.badRequest().body("Cannot apply for the event, it is already full.");
            }

            // Check if the user has already applied to the event
            boolean alreadyApplied = applicationRepository.existsByUserAndEvent(user, event);

            if (alreadyApplied) {
                return ResponseEntity.badRequest().body("User has already applied to this event.");
            } else {
                application.setEvent(event);
                application.setUser(user);
                application.setApplicantName(user.getName());
                application.setEventName(event.getName());
                application.setApplicationStatus("pending");
                Application savedApplication = applicationRepository.save(application);
                return ResponseEntity.ok(savedApplication);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{eventId}/applications")
    public ResponseEntity<List<Application>> getApplicationsForEvent(@PathVariable Long eventId) {
        List<Application> applications = applicationService.getApplicationsEvent(eventId);
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/applications/{userId}")
    public ResponseEntity<List<Application>> getApplicationsForUser(@PathVariable Long userId) {
        List<Application> applications = applicationService.getApplicationsUser(userId);
        return ResponseEntity.ok(applications);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Optional<Application>> updateApplication(@PathVariable(value="id") long Id, @RequestBody Application newApplication){
        Optional<Application> existingApplication = applicationService.findByID(Id);

        if (existingApplication.isPresent()) {
            Application applicationToUpdate = existingApplication.get();
            applicationToUpdate.setApplicationStatus(newApplication.getApplicationStatus());
            Event event = applicationToUpdate.getEvent();
            if(!event.getFull() && "accepted".equals(newApplication.getApplicationStatus())) {
                    event.setNumberOfApplications(event.getNumberOfApplications() + 1);
                    if (event.getNumberOfApplications() == (event.getApplicationLimit())) {
                        event.setFull(true);
                    }
                    eventRepository.save(event);
            }else if ("rejected".equals(newApplication.getApplicationStatus()) && event.getNumberOfApplications() > 0) {
                event.setNumberOfApplications(event.getNumberOfApplications() - 1);
                event.setFull(false);
            }else if(event.getFull() && "accepted".equals(newApplication.getApplicationStatus())){
                return ResponseEntity.badRequest().body(Optional.empty());
            }
            applicationService.addApplication(applicationToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(applicationToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public String withdrawApplication(@PathVariable(value = "id") long Id) {
        applicationService.deleteApplication(Id);
        return "Application Withdrawn";
    }
}
