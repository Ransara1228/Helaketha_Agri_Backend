package com.helaketha.agri_new.agri.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.helaketha.agri_new.agri.entity.ServiceBooking;

@Repository
public class ServiceBookingRepositoryImpl implements ServiceBookingRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServiceBookingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<ServiceBooking> MAPPER = (rs, rowNum) -> {
        ServiceBooking b = new ServiceBooking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setFarmerId(rs.getInt("farmer_id"));
        b.setServiceType(rs.getString("service_type"));
        Integer providerId = rs.getObject("provider_id", Integer.class);
        b.setProviderId(providerId);
        Date d = rs.getDate("booking_date");
        if (d != null) { b.setBookingDate(d.toLocalDate()); }
        java.sql.Time t = rs.getTime("booking_time");
        if (t != null) { b.setBookingTime(t.toLocalTime()); }
        b.setTotalCost(rs.getBigDecimal("total_cost"));
        b.setStatus(rs.getString("status"));
        return b;
    };

    @Override
    public int insert(ServiceBooking booking) {
        String sql = "INSERT INTO service_booking (farmer_id, service_type, provider_id, booking_date, booking_time, total_cost, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, booking.getFarmerId());
            ps.setString(2, booking.getServiceType());
            ps.setObject(3, booking.getProviderId());
            ps.setObject(4, booking.getBookingDate() != null ? Date.valueOf(booking.getBookingDate()) : null);
            ps.setObject(5, booking.getBookingTime() != null ? Time.valueOf(booking.getBookingTime()) : null);
            ps.setObject(6, booking.getTotalCost());
            ps.setString(7, booking.getStatus() != null ? booking.getStatus() : "Pending");
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<ServiceBooking> findAll() {
        return jdbcTemplate.query("SELECT * FROM service_booking", MAPPER);
    }

    @Override
    public Optional<ServiceBooking> findById(Integer id) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE booking_id=?", MAPPER, id).stream().findFirst();
    }

    @Override
    public List<ServiceBooking> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE status=?", MAPPER, status);
    }

    @Override
    public List<ServiceBooking> findByFarmerId(Integer farmerId) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE farmer_id=?", MAPPER, farmerId);
    }

    @Override
    public List<ServiceBooking> findByDate(LocalDate date) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE booking_date=?", MAPPER, Date.valueOf(date));
    }

    @Override
    public List<ServiceBooking> findByDateRange(LocalDate start, LocalDate end) {
        return jdbcTemplate.query("SELECT * FROM service_booking WHERE booking_date BETWEEN ? AND ?", MAPPER, Date.valueOf(start), Date.valueOf(end));
    }

    @Override
    public int update(ServiceBooking booking) {
        String sql = "UPDATE service_booking SET farmer_id=?, service_type=?, provider_id=?, booking_date=?, booking_time=?, total_cost=?, status=? WHERE booking_id=?";
        return jdbcTemplate.update(sql,
                booking.getFarmerId(),
                booking.getServiceType(),
                booking.getProviderId(),
                booking.getBookingDate() != null ? Date.valueOf(booking.getBookingDate()) : null,
                booking.getBookingTime() != null ? Time.valueOf(booking.getBookingTime()) : null,
                booking.getTotalCost(),
                booking.getStatus() != null ? booking.getStatus() : "Pending",
                booking.getBookingId());
    }

    @Override
    public int updateStatus(Integer id, String status) {
        return jdbcTemplate.update("UPDATE service_booking SET status=? WHERE booking_id=?", status, id);
    }

    @Override
    public int delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM service_booking WHERE booking_id=?", id);
    }
}
