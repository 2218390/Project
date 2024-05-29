package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.service.UslugaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Uslugas")
public class UslugaController {

    private final UslugaRepository uslugaRepository;

    private final UserRepository userRepository;

    private final UslugaService uslugaService;
    @Autowired
    public UslugaController(UslugaRepository uslugaRepository, UserRepository userRepository, UslugaService uslugaService) {
        this.uslugaRepository = uslugaRepository;
        this.userRepository = userRepository;
        this.uslugaService = uslugaService;
    }

    @PreAuthorize("hasRole('ROLE_MASTER')")
    @PostMapping(path="/create/{userId}")
    public ResponseEntity<?> createUsluga(@PathVariable Long userId, @RequestBody Usluga usluga) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            usluga.setUser(user);
            Usluga savedUsluga = uslugaRepository.save(usluga);
            return ResponseEntity.ok(savedUsluga);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/usluga/{id}")
    public ResponseEntity<Optional<Usluga>> updateUsluga(@PathVariable(value="id") long Id, @RequestBody Usluga newUsluga ){
        Optional<Usluga> existingUsluga = uslugaService.findByID(Id);
        if (existingUsluga.isPresent()) {
            Usluga uslugaToUpdate = existingUsluga.get();
            uslugaToUpdate.setName(newUsluga.getName());
            uslugaToUpdate.setDescription(newUsluga.getDescription());
            uslugaToUpdate.setCoordinates(newUsluga.getCoordinates());
            uslugaToUpdate.setLocation(newUsluga.getLocation());
            uslugaService.saveUsluga(uslugaToUpdate);
            return new ResponseEntity<>(Optional.ofNullable(uslugaToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usluga>> getAllUslugas() {
        List<Usluga> Uslugas = uslugaRepository.findAll();
        return ResponseEntity.ok(Uslugas);
    }
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @GetMapping("/{userId}/Uslugas")
    public ResponseEntity<List<Usluga>> getUslugasByUserId(@PathVariable Long userId) {
        List<Usluga> uslugas = uslugaService.getUslugas(userId);
        return ResponseEntity.ok(uslugas);
    }
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @DeleteMapping("/{id}")
    public String deleteUsluga(@PathVariable(value = "id") long Id) {
        uslugaService.deleteUsluga(Id);
        return "Usluga Deleted";
    }
    @GetMapping("/search")
    public ResponseEntity<List<Usluga>> searchUslugasByName(@RequestParam String name) {
        List<Usluga> uslugas = uslugaRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(uslugas);
    }
    @GetMapping("/city")
    public ResponseEntity<List<Usluga>> getUslugasByLocation(@RequestParam String location) {
        List<Usluga> Uslugas = uslugaRepository.findByLocation(location);
        return ResponseEntity.ok(Uslugas);
    }
}
