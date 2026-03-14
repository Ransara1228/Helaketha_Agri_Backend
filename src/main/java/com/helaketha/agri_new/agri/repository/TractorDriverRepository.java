package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import com.helaketha.agri_new.agri.entity.TractorDriver;

public interface TractorDriverRepository {
    int insert(TractorDriver driver);
    List<TractorDriver> findAll();
    Optional<TractorDriver> findById(int id);
    int update(TractorDriver driver);
    int delete(int id);
}
