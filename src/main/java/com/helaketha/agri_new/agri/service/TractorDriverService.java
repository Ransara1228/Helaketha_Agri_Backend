package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.TractorDriverRepository;
import com.helaketha.agri_new.agri.entity.TractorDriver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TractorDriverService {

    private final TractorDriverRepository dao;

    public TractorDriverService(TractorDriverRepository dao) {
        this.dao = dao;
    }

    public TractorDriver create(TractorDriver d) {
        int id = dao.insert(d);
        d.setTractorDriverId(id);
        return d;
    }

    public List<TractorDriver> findAll() {
        return dao.findAll();
    }

    public Optional<TractorDriver> findById(int id) {
        return dao.findById(id);
    }

    public TractorDriver update(int id, TractorDriver d) {
        d.setTractorDriverId(id);
        dao.update(d);
        return d;
    }

    public boolean delete(int id) {
        return dao.delete(id) > 0;
    }
}