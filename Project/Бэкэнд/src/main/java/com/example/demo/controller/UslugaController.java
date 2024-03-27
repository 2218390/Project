package com.example.demo.controller;

import com.example.demo.model.Master;
import com.example.demo.model.Usluga;
import com.example.demo.repository.MasterRepository;
import com.example.demo.repository.UslugaRepository;
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
    private MasterRepository masterRepository;

    @Autowired
    UslugaService uslugaService;

    @PostMapping(path="/create/{masterId}")
    public ResponseEntity<?> createUsluga(@PathVariable Long userId, @RequestBody Usluga usluga) {
        Optional<Master> optionalMaster = masterRepository.findById(userId);
        if (optionalMaster.isPresent()) {
            Master master = optionalMaster.get();
            usluga.setMaster(master);
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
    @GetMapping("/{masterId}/Uslugas")
    public ResponseEntity<List<Usluga>> getUslugasByUserId(@PathVariable Long userId) {
        List<Usluga> uslugas = uslugaService.getUslugas(userId);
        return ResponseEntity.ok(uslugas);
    }
    @DeleteMapping("/{id}")
    public String deleteUsluga(@PathVariable(value = "id") long Id) {
        uslugaService.deleteUsluga(Id);
        return "Usluga Deleted";
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
