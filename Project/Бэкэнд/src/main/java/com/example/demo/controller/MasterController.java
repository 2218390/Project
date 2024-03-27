package com.example.demo.controller;

import com.example.demo.dto.UserPostDTO;
import com.example.demo.model.Master;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.service.MasterService;
import com.example.demo.service.UserService;
import com.example.demo.service.UslugaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MasterController {
    @Autowired
    MasterService masterService;

    @Autowired
    UslugaService uslugaService;


    // Get All Users
    @GetMapping("/master")
    public List<Master> getMasters() {
        return masterService.getMasters();
    }

    @PostMapping("/master")
    public ResponseEntity<Optional<Master>> addMaster(@RequestBody UserPostDTO newUserDTO) {

        if (newUserDTO.getName()==null ||
                newUserDTO.getEmail()==null ||
                newUserDTO.getPassword()==null) {
            return new ResponseEntity<>(Optional.ofNullable(null), HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Master newMaster = new Master(newUserDTO.getName(), newUserDTO.getEmail(), newUserDTO.getTelephone_number(),
                encoder.encode(newUserDTO.getPassword()),null,null,null);
        masterService.saveMaster(newMaster);
        return new ResponseEntity<>(Optional.ofNullable(newMaster),HttpStatus.CREATED);

    }
    @PutMapping("/master/{id}")
    public ResponseEntity<Optional<Master>> updateMaster(@PathVariable(value="id") long Id, @RequestBody Master newMaster ){
        Optional<Master> existingMaster = masterService.findByID(Id);

        if (existingMaster.isPresent()) {
            Master masterToUpdate = existingMaster.get();
            masterToUpdate.setName(newMaster.getName());
            masterToUpdate.setEmail(newMaster.getEmail());
            masterService.saveMaster(masterToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(masterToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path="/master/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Optional<Master>> updateProfilePicture(@RequestPart(value = "file") MultipartFile file, @PathVariable(value="id") long Id) {
        Optional<Master> existingMaster = masterService.findByID(Id);

        if (existingMaster.isPresent()) {
            Master masterToUpdate = existingMaster.get();
            if (file != null && !file.isEmpty()) {
                try {
                    masterToUpdate.setProfilePicture(file.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            masterService.saveMaster(masterToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(masterToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
    // Get User by ID
    @GetMapping("/master/{id}")
    public Optional<Master> getUserById(@PathVariable(value = "id") long Id) {
        return masterService.findByID(Id);
    }


    //Delete a User by ID
    @DeleteMapping("/master/{id}")
    public String deleteUser(@PathVariable(value = "id") long Id) {
        masterService.deleteMaster(Id);
        return "User Deleted";
    }

    //Get User by Email
    @GetMapping("master/email/{email}")
    public Optional<Master> getUserByEmail(@PathVariable(value = "email") String email) {
        return Optional.ofNullable(masterService.findByEmail(email));
    }
    @PostMapping("/master/{masterId}/Uslugas/{UslugaId}/favorite")
    public ResponseEntity<String> addUslugaToFavorites(@PathVariable Long masterId, @PathVariable Long UslugaId) {
        Optional<Master> optionalMaster = masterService.findByID(masterId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(UslugaId);

        if (optionalMaster.isPresent() && optionalUsluga.isPresent()) {
            Master master = optionalMaster.get();
            Usluga Usluga = optionalUsluga.get();
            if (Usluga.getMaster().getId().equals(masterId)) {
                return ResponseEntity.badRequest().body("Cannot add own Usluga to favorites.");
            }
            master.getFavoriteUslugas().add(Usluga);
            masterService.saveMaster(master);

            return ResponseEntity.ok("Usluga added to favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/master/{masterId}/Uslugas/{UslugaId}/favorite")
    public ResponseEntity<String> removeUslugaFromFavorites(@PathVariable Long masterId, @PathVariable Long uslugaId) {
        Optional<Master> optionalUser = masterService.findByID(masterId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(uslugaId);

        if (optionalUser.isPresent() && optionalUsluga.isPresent()) {
            Master master = optionalUser.get();
            Usluga Usluga = optionalUsluga.get();

            master.getFavoriteUslugas().remove(Usluga);
            masterService.saveMaster(master);

            return ResponseEntity.ok("Usluga removed from favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{masterId}/favorite-Uslugas")
    public ResponseEntity<List<Usluga>> getFavoriteUslugas(@PathVariable Long masterId) {
        Optional<Master> optionalUser = masterService.findByID(masterId);

        if (optionalUser.isPresent()) {
            List<Usluga> favoriteUslugas = optionalUser.get().getFavoriteUslugas();
            return ResponseEntity.ok(favoriteUslugas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
