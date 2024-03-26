package com.example.demo.controller;

import com.example.demo.model.Usluga;
import com.example.demo.model.User;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UslugaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Uslugas")
public class UslugaController {

    @Autowired
    private UslugaRepository UslugaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UslugaService uslugaService;

    @PostMapping(path="/create/{userId}")
    public ResponseEntity<?> createUsluga(@PathVariable Long userId, @RequestBody Usluga usluga) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            usluga.setUser(user);
            Usluga savedUsluga = UslugaRepository.save(usluga);
            return ResponseEntity.ok(savedUsluga);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Usluga>> getAllUslugas() {
        List<Usluga> Uslugas = UslugaRepository.findAll();
        return ResponseEntity.ok(Uslugas);
    }
    @GetMapping("/{userId}/Uslugas")
    public ResponseEntity<List<Usluga>> getUslugasByUserId(@PathVariable Long userId) {
        List<Usluga> uslugas = uslugaService.getUslugas(userId);
        return ResponseEntity.ok(uslugas);
    }
    @DeleteMapping("/{id}")
    public String deleteUsluga(@PathVariable(value = "id") long Id) {
        uslugaService.deleteUsluga(Id);
        return "User Deleted";
    }
    @GetMapping("/search")
    public ResponseEntity<List<Usluga>> searchUslugasByName(@RequestParam String name) {
        List<Usluga> uslugas = UslugaRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(uslugas);
    }
    @GetMapping("/city")
    public ResponseEntity<List<Usluga>> getUslugasByLocation(@RequestParam String location) {
        List<Usluga> Uslugas = UslugaRepository.findByLocation(location);
        return ResponseEntity.ok(Uslugas);
    }
}
