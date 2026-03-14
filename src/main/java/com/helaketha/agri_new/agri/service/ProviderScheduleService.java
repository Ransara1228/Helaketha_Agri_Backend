package com.helaketha.agri_new.agri.service;

import com.helaketha.agri_new.agri.entity.ProviderSchedule;
import com.helaketha.agri_new.agri.repository.ProviderScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProviderScheduleService {

    private final ProviderScheduleRepository repository;

    public ProviderScheduleService(ProviderScheduleRepository repository) {
        this.repository = repository;
    }

    public ProviderSchedule create(ProviderSchedule schedule) {
        int id = repository.insert(schedule);
        schedule.setScheduleId(id);
        return schedule;
    }

    public List<ProviderSchedule> findAll() {
        return repository.findAll();
    }

    public Optional<ProviderSchedule> findById(int id) {
        return repository.findById(id);
    }

    public List<ProviderSchedule> findByProviderTypeAndProviderId(String providerType, Integer providerId) {
        return repository.findByProviderTypeAndProviderId(providerType, providerId);
    }

    public List<ProviderSchedule> findByProviderTypeAndDate(String providerType, LocalDate date) {
        return repository.findByProviderTypeAndDate(providerType, date);
    }

    public List<ProviderSchedule> findAvailableSchedules(String providerType, LocalDate date) {
        return repository.findAvailableSchedules(providerType, date);
    }

    public ProviderSchedule update(int id, ProviderSchedule schedule) {
        schedule.setScheduleId(id);
        repository.update(schedule);
        return schedule;
    }

    public boolean delete(int id) {
        return repository.delete(id) > 0;
    }

    public ProviderSchedule bookSchedule(int scheduleId) {
        return findById(scheduleId).map(schedule -> {
            schedule.setIsBooked(true);
            repository.update(schedule);
            return schedule;
        }).orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));
    }

    public ProviderSchedule releaseSchedule(int scheduleId) {
        return findById(scheduleId).map(schedule -> {
            schedule.setIsBooked(false);
            repository.update(schedule);
            return schedule;
        }).orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));
    }
}

