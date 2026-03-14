package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.helaketha.agri_new.agri.entity.TractorDriver;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class TractorDriverRepositoryImpl implements TractorDriverRepository {

    private final JdbcTemplate jdbcTemplate;

    public TractorDriverRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<TractorDriver> MAPPER = (rs, rowNum) -> {
        TractorDriver d = new TractorDriver();
        d.setTractorDriverId(rs.getInt("tractor_driver_id"));
        d.setName(rs.getString("name"));
        d.setPhone(rs.getString("phone"));
        Integer machineQty = rs.getObject("machine_quantity", Integer.class);
        d.setMachineQuantity(machineQty);
        d.setPricePerAcre(rs.getBigDecimal("price_per_acre"));
        d.setUsername(rs.getString("username"));

        return d;
    };

    @Override
    public int insert(TractorDriver d) {
        String sql = "INSERT INTO tractor_drivers (name, phone, machine_quantity, price_per_acre, username) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getName());
            ps.setString(2, d.getPhone());
            ps.setObject(3, d.getMachineQuantity());
            ps.setObject(4, d.getPricePerAcre());
            ps.setString(5, d.getUsername());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<TractorDriver> findAll() {
        return jdbcTemplate.query(
                "SELECT tractor_driver_id, name, phone, machine_quantity, price_per_acre, username FROM tractor_drivers",
                MAPPER
        );
    }

    @Override
    public Optional<TractorDriver> findById(int id) {
        return jdbcTemplate.query(
                "SELECT tractor_driver_id, name, phone, machine_quantity, price_per_acre, username FROM tractor_drivers WHERE tractor_driver_id = ?",
                MAPPER,
                id
        ).stream().findFirst();
    }

    @Override
    public int update(TractorDriver d) {
        return jdbcTemplate.update(
                "UPDATE tractor_drivers SET name = ?, phone = ?, machine_quantity = ?, price_per_acre = ?, username = ? WHERE tractor_driver_id = ?",
                d.getName(), d.getPhone(), d.getMachineQuantity(), d.getPricePerAcre(), d.getUsername(), d.getTractorDriverId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(
                "DELETE FROM tractor_drivers WHERE tractor_driver_id = ?",
                id
        );
    }
}
