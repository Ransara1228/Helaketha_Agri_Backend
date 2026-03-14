package com.helaketha.agri_new.agri.repository;

import com.helaketha.agri_new.agri.entity.ProviderSchedule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ProviderScheduleRepositoryImpl implements ProviderScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProviderScheduleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ProviderSchedule> MAPPER = (rs, rowNum) -> {
        ProviderSchedule schedule = new ProviderSchedule();
        schedule.setScheduleId(rs.getInt("schedule_id"));
        schedule.setProviderType(rs.getString("provider_type"));
        schedule.setProviderId(rs.getInt("provider_id"));
        
        Date date = rs.getDate("available_date");
        schedule.setAvailableDate(date != null ? date.toLocalDate() : null);
        
        // Handle boolean - MySQL uses TINYINT(1) which maps to Boolean
        schedule.setIsBooked(rs.getBoolean("is_booked"));
        
        return schedule;
    };

    @Override
    public int insert(ProviderSchedule schedule) {
        String sql = "INSERT INTO provider_schedules (provider_type, provider_id, available_date, is_booked) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getProviderType());
            ps.setInt(2, schedule.getProviderId());
            ps.setDate(3, schedule.getAvailableDate() != null ? Date.valueOf(schedule.getAvailableDate()) : null);
            ps.setBoolean(4, schedule.getIsBooked() != null ? schedule.getIsBooked() : false);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public int update(ProviderSchedule schedule) {
        String sql = "UPDATE provider_schedules SET provider_type=?, provider_id=?, available_date=?, is_booked=? WHERE schedule_id=?";
        return jdbcTemplate.update(sql,
                schedule.getProviderType(),
                schedule.getProviderId(),
                schedule.getAvailableDate() != null ? Date.valueOf(schedule.getAvailableDate()) : null,
                schedule.getIsBooked() != null ? schedule.getIsBooked() : false,
                schedule.getScheduleId());
    }

    @Override
    public List<ProviderSchedule> findAll() {
        String sql = "SELECT * FROM provider_schedules ORDER BY available_date DESC";
        return jdbcTemplate.query(sql, MAPPER);
    }

    @Override
    public Optional<ProviderSchedule> findById(int id) {
        String sql = "SELECT * FROM provider_schedules WHERE schedule_id = ?";
        return jdbcTemplate.query(sql, MAPPER, id).stream().findFirst();
    }

    @Override
    public List<ProviderSchedule> findByProviderTypeAndProviderId(String providerType, Integer providerId) {
        String sql = "SELECT * FROM provider_schedules WHERE provider_type = ? AND provider_id = ? ORDER BY available_date";
        return jdbcTemplate.query(sql, MAPPER, providerType, providerId);
    }

    @Override
    public List<ProviderSchedule> findByProviderTypeAndDate(String providerType, LocalDate date) {
        String sql = "SELECT * FROM provider_schedules WHERE provider_type = ? AND available_date = ? ORDER BY provider_id";
        return jdbcTemplate.query(sql, MAPPER, providerType, Date.valueOf(date));
    }

    @Override
    public List<ProviderSchedule> findAvailableSchedules(String providerType, LocalDate date) {
        String sql = "SELECT * FROM provider_schedules WHERE provider_type = ? AND available_date = ? AND is_booked = false ORDER BY provider_id";
        return jdbcTemplate.query(sql, MAPPER, providerType, Date.valueOf(date));
    }

    @Override
    public int delete(int id) {
        String sql = "DELETE FROM provider_schedules WHERE schedule_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

