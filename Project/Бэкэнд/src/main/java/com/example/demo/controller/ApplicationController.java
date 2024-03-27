package com.example.demo.controller;

import com.example.demo.model.Application;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.UslugaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UslugaRepository uslugaRepository;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private UslugaService uslugaService;

    @PostMapping("/apply/{userId}/{uslugaId}")
    public ResponseEntity<?> applyForUsluga(@PathVariable Long uslugaId, @PathVariable Long userId, @RequestBody Application application) {
        Optional<Usluga> optionalUsluga = uslugaRepository.findById(uslugaId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUsluga.isPresent() && optionalUser.isPresent()) {
            Usluga usluga = optionalUsluga.get();
            User user = optionalUser.get();
            boolean alreadyApplied = applicationRepository.existsByUserAndUsluga(user, usluga);

            if (alreadyApplied) {
                return ResponseEntity.badRequest().body("User has already applied to this event.");
            } else {
                application.setUsluga(usluga);
                application.setUser(user);
                application.setApplicantName(user.getName());
                application.setApplicantEmail(user.getEmail());
                application.setUslugaName(usluga.getName());
                application.setApplicationStatus("pending");
                Application savedApplication = applicationRepository.save(application);
                return ResponseEntity.ok(savedApplication);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{uslugaId}/applications")
    public ResponseEntity<List<Application>> getApplicationsForUsluga(@PathVariable Long uslugaId) {
        List<Application> applications = applicationService.getApplicationsUsluga(uslugaId);
        return ResponseEntity.ok(applications);
    }
    @GetMapping("/applications/{userId}")
    public ResponseEntity<List<Application>> getApplicationsForUser(@PathVariable Long userId) {
        List<Application> applications = applicationService.getApplicationsUser(userId);
        return ResponseEntity.ok(applications);
    }
    @DeleteMapping("/{id}")
    public String withdrawApplication(@PathVariable(value = "id") long Id) {
        applicationService.deleteApplication(Id);
        return "Application Withdrawn";
    }
}
