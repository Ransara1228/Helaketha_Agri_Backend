package com.helaketha.agri_new.agri.repository;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;
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
public class FertilizerSupplierRepositoryImpl implements FertilizerSupplierRepository {

    private final JdbcTemplate jdbcTemplate;

    public FertilizerSupplierRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<FertilizerSupplier> MAPPER = (rs, rowNum) -> {
        FertilizerSupplier s = new FertilizerSupplier();
        s.setSupplierId(rs.getInt("supplier_id"));
        s.setName(rs.getString("name"));
        s.setPhone(rs.getString("phone"));
        s.setEmail(rs.getString("email")); // Map email
        s.setFertilizerType(rs.getString("fertilizer_type"));
        s.setStockQuantityLiters(rs.getObject("stock_quantity_liters", Integer.class));
        s.setPricePerLiter(rs.getBigDecimal("price_per_liter"));
        s.setUsername(rs.getString("username"));
        try {
            s.setKeycloakUserId(rs.getString("keycloak_user_id")); // Map Keycloak ID
        } catch (Exception e) {
            s.setKeycloakUserId(null);
        }
        return s;
    };

    @Override
    public int insert(FertilizerSupplier s) {
        String sql = "INSERT INTO fertilizer_suppliers (name, phone, email, fertilizer_type, stock_quantity_liters, price_per_liter, username, keycloak_user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getFertilizerType());
            ps.setObject(5, s.getStockQuantityLiters());
            ps.setObject(6, s.getPricePerLiter());
            ps.setString(7, s.getUsername());
            ps.setString(8, s.getKeycloakUserId()); // Save the Keycloak ID
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<FertilizerSupplier> findAll() {
        return jdbcTemplate.query("SELECT * FROM fertilizer_suppliers", MAPPER);
    }

    @Override
    public Optional<FertilizerSupplier> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM fertilizer_suppliers WHERE supplier_id = ?", MAPPER, id).stream().findFirst();
    }

    @Override
    public int update(FertilizerSupplier s) {
        return jdbcTemplate.update(
                "UPDATE fertilizer_suppliers SET name=?, phone=?, email=?, fertilizer_type=?, stock_quantity_liters=?, price_per_liter=?, username=?, keycloak_user_id=? WHERE supplier_id=?",
                s.getName(), s.getPhone(), s.getEmail(), s.getFertilizerType(), s.getStockQuantityLiters(), s.getPricePerLiter(), s.getUsername(), s.getKeycloakUserId(), s.getSupplierId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM fertilizer_suppliers WHERE supplier_id = ?", id);
    }
}