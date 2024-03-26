package com.example.demo.controller;

import com.example.demo.model.Application;
import com.example.demo.model.Usluga;
import com.example.demo.model.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.UslugaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UslugaRepository uslugaRepository;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UslugaService uslugaService;

    @PostMapping("/apply/{userId}/{eventId}")
    public ResponseEntity<?> applyForEvent(@PathVariable Long eventId, @PathVariable Long userId, @RequestBody Application application) {
        Optional<Usluga> optionalEvent = uslugaRepository.findById(eventId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalEvent.isPresent() && optionalUser.isPresent()) {
            Usluga usluga = optionalEvent.get();
            User user = optionalUser.get();
            if (usluga.getUser().getId().equals(user.getId())) {
                return ResponseEntity.badRequest().body("Cannot apply for your own event.");
            }

            // Check if the user has already applied to the event
            boolean alreadyApplied = applicationRepository.existsByUserAndUsluga(user, usluga);

            if (alreadyApplied) {
                return ResponseEntity.badRequest().body("User has already applied to this event.");
            } else {
                application.setUsluga(usluga);
                application.setUser(user);
                application.setApplicantName(user.getName());
                application.setApplicantEmail(user.getEmail());
                application.setEventName(usluga.getName());
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
        List<Application> applications = applicationService.getApplicationsUser(userId).stream()
                .filter(application -> !application.getUsluga().getIsFinished()) // Filter events not marked as finished// Filter applications for the specified user
                .collect(Collectors.toList());
        return ResponseEntity.ok(applications);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Optional<Application>> updateApplication(@PathVariable(value="id") long Id, @RequestBody Application newApplication){
        Optional<Application> existingApplication = applicationService.findByID(Id);

        if (existingApplication.isPresent()) {
            Application applicationToUpdate = existingApplication.get();
            applicationToUpdate.setApplicationStatus(newApplication.getApplicationStatus());
            Usluga usluga = applicationToUpdate.getEvent();
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
