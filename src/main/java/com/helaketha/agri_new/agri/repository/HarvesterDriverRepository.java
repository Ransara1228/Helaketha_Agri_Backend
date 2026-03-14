package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import com.helaketha.agri_new.agri.entity.HarvesterDriver;

public interface HarvesterDriverRepository {
    int insert(HarvesterDriver driver);
    List<HarvesterDriver> findAll();
    Optional<HarvesterDriver> findById(int id);
    int update(HarvesterDriver driver);
    int delete(int id);
}
