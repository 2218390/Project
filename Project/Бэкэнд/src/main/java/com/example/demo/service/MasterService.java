package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Master;
import com.example.demo.repository.MasterRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class MasterService {
    @Autowired
    MasterRepository masterRepository;

    public MasterService() {
        super();
        // TODO Auto-generated constructor stub
    }


    public List<Master> getMasters() {
        return (List<Master>) masterRepository.findAll();
    }

    public void saveMaster(Master newMaster) {
        masterRepository.save(newMaster);
    }

    public Optional<Master> findByID(Long id) {
        return masterRepository.findById(id);
    }

    public void deleteMaster(Long id) {
        Master master = masterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Master", "id", id));
        masterRepository.delete(master);
    }

    public Master findByEmail(String email) {
        return masterRepository.findByEmail(email);
    }
}
