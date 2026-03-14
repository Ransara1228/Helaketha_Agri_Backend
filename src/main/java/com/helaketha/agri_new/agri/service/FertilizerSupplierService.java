package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.FertilizerSupplierRepository;
import com.helaketha.agri_new.agri.entity.FertilizerSupplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FertilizerSupplierService {

    private final FertilizerSupplierRepository dao;

    public FertilizerSupplierService(FertilizerSupplierRepository dao) {
        this.dao = dao;
    }

    public FertilizerSupplier create(FertilizerSupplier s) {
        int id = dao.insert(s);
        s.setSupplierId(id);
        return s;
    }

    public List<FertilizerSupplier> findAll() {
        return dao.findAll();
    }

    public Optional<FertilizerSupplier> findById(int id) {
        return dao.findById(id);
    }

    public FertilizerSupplier update(int id, FertilizerSupplier s) {
        s.setSupplierId(id);
        dao.update(s);
        return s;
    }

    public boolean delete(int id) {
        return dao.delete(id) > 0;
    }
}