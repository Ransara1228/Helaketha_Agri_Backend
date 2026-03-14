package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.repository.ServiceBookingRepository;
import com.helaketha.agri_new.agri.entity.ServiceBooking;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceBookingService {

    private final ServiceBookingRepository dao;

    public ServiceBookingService(ServiceBookingRepository dao) {
        this.dao = dao;
    }

    public ServiceBooking save(ServiceBooking sb) {
        int id = dao.insert(sb);
        sb.setBookingId(id);
        return sb;
    }

    public List<ServiceBooking> findAll() { return dao.findAll(); }

    public Optional<ServiceBooking> findById(Integer id) { return dao.findById(id); }

    public List<ServiceBooking> findByStatus(String status) { return dao.findByStatus(status); }

    public List<ServiceBooking> findByFarmer(Integer farmerId) { return dao.findByFarmerId(farmerId); }

    public List<ServiceBooking> findByDate(LocalDate date) { return dao.findByDate(date); }

    public List<ServiceBooking> findByDateRange(LocalDate start, LocalDate end) { return dao.findByDateRange(start, end); }

    public ServiceBooking update(Integer id, ServiceBooking sb) {
        sb.setBookingId(id);
        dao.update(sb);
        return sb;
    }

    public ServiceBooking updateStatus(Integer id, String status) {
        dao.updateStatus(id, status);
        return dao.findById(id).orElse(null);
    }

    public boolean delete(Integer id) { return dao.delete(id) > 0; }
}
