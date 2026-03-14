package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.HarvesterDriverRepository;
import com.helaketha.agri_new.agri.entity.HarvesterDriver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HarvesterDriverService {

    private final HarvesterDriverRepository dao;

    public HarvesterDriverService(HarvesterDriverRepository dao) {
        this.dao = dao;
    }

    public HarvesterDriver create(HarvesterDriver d) {
        int id = dao.insert(d);
        d.setHarvesterDriverId(id);
        return d;
    }

    public List<HarvesterDriver> findAll() {
        return dao.findAll();
    }

    public Optional<HarvesterDriver> findById(int id) {
        return dao.findById(id);
    }

    public HarvesterDriver update(int id, HarvesterDriver d) {
        d.setHarvesterDriverId(id);
        dao.update(d);
        return d;
    }

    public boolean delete(int id) {
        return dao.delete(id) > 0;
    }
}