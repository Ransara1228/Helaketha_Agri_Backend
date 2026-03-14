package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import com.helaketha.agri_new.agri.entity.Farmer;

public interface FarmerRepository {
    int insert(Farmer farmer);               // returns generated id
    List<Farmer> findAll();
    Optional<Farmer> findById(int id);
    int update(Farmer farmer);               // returns rows affected
    int delete(int id);                      // returns rows affected
}
