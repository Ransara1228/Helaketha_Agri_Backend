package com.helaketha.agri_new.agri.repository;

import com.helaketha.agri_new.agri.entity.ProviderSchedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProviderScheduleRepository {
    int insert(ProviderSchedule schedule);               // returns generated id
    List<ProviderSchedule> findAll();
    Optional<ProviderSchedule> findById(int id);
    List<ProviderSchedule> findByProviderTypeAndProviderId(String providerType, Integer providerId);
    List<ProviderSchedule> findByProviderTypeAndDate(String providerType, LocalDate date);
    List<ProviderSchedule> findAvailableSchedules(String providerType, LocalDate date);
    int update(ProviderSchedule schedule);               // returns rows affected
    int delete(int id);                                  // returns rows affected
}

