package com.example.demo.repository;

import com.example.demo.model.Master;
import org.springframework.data.repository.CrudRepository;

public interface MasterRepository extends CrudRepository<Master,Long> {
    Master findByEmail(String email);
}
