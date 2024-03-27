package com.example.demo.controller;

import com.example.demo.dto.UserPostDTO;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
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


@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UslugaService uslugaService;


    // Get All Users
    @GetMapping("/user")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/user")
    public ResponseEntity<Optional<User>> addUser(@RequestBody UserPostDTO newUserDTO) {

        if (newUserDTO.getName()==null ||
                newUserDTO.getEmail()==null ||
                newUserDTO.getPassword()==null) {
            return new ResponseEntity<>(Optional.ofNullable(null), HttpStatus.BAD_REQUEST);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User newUser = new User(newUserDTO.getName(), newUserDTO.getEmail(), newUserDTO.getTelephone_number(),
                encoder.encode(newUserDTO.getPassword()),null,null);
        userService.saveUser(newUser);
        return new ResponseEntity<>(Optional.ofNullable(newUser),HttpStatus.CREATED);

    }
    @PutMapping("/user/{id}")
    public ResponseEntity<Optional<User>> updateUser(@PathVariable(value="id") long Id, @RequestBody User newUser ){
        Optional<User> existingUser = userService.findByID(Id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setName(newUser.getName());
            userToUpdate.setEmail(newUser.getEmail());
            userService.saveUser(userToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(userToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path="/user/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Optional<User>> updateProfilePicture(@RequestPart(value = "file") MultipartFile file, @PathVariable(value="id") long Id) {
        Optional<User> existingUser = userService.findByID(Id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            if (file != null && !file.isEmpty()) {
                try {
                    userToUpdate.setProfilePicture(file.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            userService.saveUser(userToUpdate);

            return new ResponseEntity<>(Optional.ofNullable(userToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
    // Get User by ID
    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable(value = "id") long Id) {
        return userService.findByID(Id);
    }


    //Delete a User by ID
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable(value = "id") long Id) {
        userService.deleteUser(Id);
        return "User Deleted";
    }

    //Get User by Email
    @GetMapping("user/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable(value = "email") String email) {
        return Optional.ofNullable(userService.findByEmail(email));
    }
    @PostMapping("/user/{userId}/Uslugas/{UslugaId}/favorite")
    public ResponseEntity<String> addUslugaToFavorites(@PathVariable Long userId, @PathVariable Long UslugaId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(UslugaId);

        if (optionalUser.isPresent() && optionalUsluga.isPresent()) {
            User user = optionalUser.get();
            Usluga Usluga = optionalUsluga.get();
            user.getFavoriteUslugas().add(Usluga);
            userService.saveUser(user);

            return ResponseEntity.ok("Usluga added to favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}/Uslugas/{UslugaId}/favorite")
    public ResponseEntity<String> removeUslugaFromFavorites(@PathVariable Long userId, @PathVariable Long uslugaId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Usluga> optionalUsluga = uslugaService.findByID(uslugaId);

        if (optionalUser.isPresent() && optionalUsluga.isPresent()) {
            User user = optionalUser.get();
            Usluga Usluga = optionalUsluga.get();

            user.getFavoriteUslugas().remove(Usluga);
            userService.saveUser(user);

            return ResponseEntity.ok("Usluga removed from favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/favorite-Uslugas")
    public ResponseEntity<List<Usluga>> getFavoriteUslugas(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findByID(userId);

        if (optionalUser.isPresent()) {
            List<Usluga> favoriteUslugas = optionalUser.get().getFavoriteUslugas();
            return ResponseEntity.ok(favoriteUslugas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
