package com.helaketha.agri_new.agri.repository;

import com.helaketha.agri_new.agri.entity.ServiceBooking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServiceBookingRepository {
    int insert(ServiceBooking booking);                      // returns generated id
    List<ServiceBooking> findAll();
    Optional<ServiceBooking> findById(Integer id);
    List<ServiceBooking> findByStatus(String status);
    List<ServiceBooking> findByFarmerId(Integer farmerId);
    List<ServiceBooking> findByDate(LocalDate date);
    List<ServiceBooking> findByDateRange(LocalDate start, LocalDate end);
    int update(ServiceBooking booking);                      // rows affected
    int updateStatus(Integer id, String status);             // rows affected
    int delete(Integer id);                                  // rows affected
}
