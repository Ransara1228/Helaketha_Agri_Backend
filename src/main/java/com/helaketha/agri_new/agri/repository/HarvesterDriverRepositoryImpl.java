package com.helaketha.agri_new.agri.repository;

import com.helaketha.agri_new.agri.entity.HarvesterDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class HarvesterDriverRepositoryImpl implements HarvesterDriverRepository {

    private final JdbcTemplate jdbcTemplate;

    public HarvesterDriverRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<HarvesterDriver> MAPPER = (rs, rowNum) -> {
        HarvesterDriver d = new HarvesterDriver();
        d.setHarvesterDriverId(rs.getInt("harvester_driver_id"));
        d.setName(rs.getString("name"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email")); // Map email
        d.setAvailableMachines(rs.getObject("available_machines", Integer.class));
        d.setPricePerAcre(rs.getBigDecimal("price_per_acre"));
        d.setUsername(rs.getString("username"));
        try {
            d.setKeycloakUserId(rs.getString("keycloak_user_id")); // Map Keycloak ID
        } catch (Exception e) {
            d.setKeycloakUserId(null);
        }
        return d;
    };

    @Override
    public int insert(HarvesterDriver d) {
        // SQL Updated to include email and keycloak_user_id
        String sql = "INSERT INTO harvester_drivers (name, phone, email, available_machines, price_per_acre, username, keycloak_user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, d.getName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getEmail());
            ps.setObject(4, d.getAvailableMachines());
            ps.setObject(5, d.getPricePerAcre());
            ps.setString(6, d.getUsername());
            ps.setString(7, d.getKeycloakUserId()); // Save the ID
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<HarvesterDriver> findAll() {
        return jdbcTemplate.query("SELECT * FROM harvester_drivers", MAPPER);
    }

    @Override
    public Optional<HarvesterDriver> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM harvester_drivers WHERE harvester_driver_id = ?", MAPPER, id).stream().findFirst();
    }

    @Override
    public int update(HarvesterDriver d) {
        return jdbcTemplate.update(
                "UPDATE harvester_drivers SET name=?, phone=?, email=?, available_machines=?, price_per_acre=?, username=?, keycloak_user_id=? WHERE harvester_driver_id=?",
                d.getName(), d.getPhone(), d.getEmail(), d.getAvailableMachines(), d.getPricePerAcre(), d.getUsername(), d.getKeycloakUserId(), d.getHarvesterDriverId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM harvester_drivers WHERE harvester_driver_id = ?", id);
    }
}