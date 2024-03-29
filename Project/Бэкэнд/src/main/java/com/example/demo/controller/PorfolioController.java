package com.example.demo.controller;

import com.example.demo.model.Portfolio;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PortfolioService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PorfolioController {
    @Autowired
    PortfolioService portfolioService;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/portfolio")
    public List<Portfolio> getUsers() {
        return portfolioService.getPortfolios();
    }

   /* @PostMapping(path="/create_portfolio/{userId}")
    public ResponseEntity<?> createPortfolio(@PathVariable Long userId, @RequestBody Portfolio portfolio) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            portfolio.setUser(user);

            return ResponseEntity.ok(savedPortfolio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @GetMapping("/portfolio/{id}")
    public Optional<Portfolio> getPortfolioById(@PathVariable(value = "id") long Id) {
        return portfolioService.findByID(Id);
    }

    @DeleteMapping("/portfolio/{id}")
    public String deleteUser(@PathVariable(value = "id") long Id) {
        portfolioService.deletePortfolio(Id);
        return "Portfolio Deleted";
    }
}
