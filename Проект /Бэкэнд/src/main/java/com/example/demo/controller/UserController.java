package com.example.demo.controller;

import com.example.demo.dto.UserPostDTO;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
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
    EventService eventService;


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
        User newUser = new User(newUserDTO.getName(), newUserDTO.getEmail(),
                encoder.encode(newUserDTO.getPassword()),null,null,null,null,null, null);
        userService.addUser(newUser);
        return new ResponseEntity<>(Optional.ofNullable(newUser),HttpStatus.CREATED);

    }
    @PutMapping("/user/{id}")
    public ResponseEntity<Optional<User>> updateUser(@PathVariable(value="id") long Id, @RequestBody User newUser ){
        Optional<User> existingUser = userService.findByID(Id);

        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setName(newUser.getName());
            userToUpdate.setEmail(newUser.getEmail());
            userToUpdate.setMission(newUser.getMission());
            userService.addUser(userToUpdate);

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
                    // Normalize the file na

                    // Save the file to the database or file system
                    // You can customize this part based on your storage strategy
                    // For simplicity, we assume storing the file in the database as a byte array
                    userToUpdate.setProfilePicture(file.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            userService.addUser(userToUpdate);

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
    @PostMapping("/user/{userId}/events/{eventId}/favorite")
    public ResponseEntity<String> addEventToFavorites(@PathVariable Long userId, @PathVariable Long eventId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Event> optionalEvent = eventService.findByID(eventId);

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();
            if (event.getUser().getId().equals(userId)) {
                return ResponseEntity.badRequest().body("Cannot add own event to favorites.");
            }
            user.getFavoriteEvents().add(event);
            userService.addUser(user);

            return ResponseEntity.ok("Event added to favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/user/{userId}/events/{eventId}/favorite")
    public ResponseEntity<String> removeEventFromFavorites(@PathVariable Long userId, @PathVariable Long eventId) {
        Optional<User> optionalUser = userService.findByID(userId);
        Optional<Event> optionalEvent = eventService.findByID(eventId);

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            user.getFavoriteEvents().remove(event);
            userService.addUser(user);

            return ResponseEntity.ok("Event removed from favorites");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userId}/favorite-events")
    public ResponseEntity<List<Event>> getFavoriteEvents(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findByID(userId);

        if (optionalUser.isPresent()) {
            List<Event> favoriteEvents = optionalUser.get().getFavoriteEvents();
            return ResponseEntity.ok(favoriteEvents);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
