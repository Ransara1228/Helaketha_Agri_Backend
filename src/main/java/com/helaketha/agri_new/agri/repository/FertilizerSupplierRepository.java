package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;

public interface FertilizerSupplierRepository {
    int insert(FertilizerSupplier supplier);
    List<FertilizerSupplier> findAll();
    Optional<FertilizerSupplier> findById(int id);
    int update(FertilizerSupplier supplier);
    int delete(int id);
}
