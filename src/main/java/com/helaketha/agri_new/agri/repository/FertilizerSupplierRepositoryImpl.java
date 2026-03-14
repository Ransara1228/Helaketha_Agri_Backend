package com.helaketha.agri_new.agri.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.helaketha.agri_new.agri.entity.FertilizerSupplier;

import java.sql.PreparedStatement;
import java.sql.Statement;

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
        s.setFertilizerType(rs.getString("fertilizer_type"));
        Integer stockQty = rs.getObject("stock_quantity_liters", Integer.class);
        s.setStockQuantityLiters(stockQty);
        s.setPricePerLiter(rs.getBigDecimal("price_per_liter"));
        s.setUsername(rs.getString("username"));

        return s;
    };

    @Override
    public int insert(FertilizerSupplier s) {
        String sql = "INSERT INTO fertilizer_suppliers (name, phone, fertilizer_type, stock_quantity_liters, price_per_liter, username) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getFertilizerType());
            ps.setObject(4, s.getStockQuantityLiters());
            ps.setObject(5, s.getPricePerLiter());
            ps.setString(6, s.getUsername());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key != null ? key.intValue() : 0;
    }

    @Override
    public List<FertilizerSupplier> findAll() {
        return jdbcTemplate.query(
                "SELECT supplier_id, name, phone, fertilizer_type, stock_quantity_liters, price_per_liter, username FROM fertilizer_suppliers",
                MAPPER
        );
    }

    @Override
    public Optional<FertilizerSupplier> findById(int id) {
        return jdbcTemplate.query(
                "SELECT supplier_id, name, phone, fertilizer_type, stock_quantity_liters, price_per_liter, username FROM fertilizer_suppliers WHERE supplier_id = ?",
                MAPPER,
                id
        ).stream().findFirst();
    }

    @Override
    public int update(FertilizerSupplier s) {
        return jdbcTemplate.update(
                "UPDATE fertilizer_suppliers SET name = ?, phone = ?, fertilizer_type = ?, stock_quantity_liters = ?, price_per_liter = ?, username = ? WHERE supplier_id = ?",
                s.getName(), s.getPhone(), s.getFertilizerType(), s.getStockQuantityLiters(), s.getPricePerLiter(), s.getUsername(), s.getSupplierId()
        );
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update(
                "DELETE FROM fertilizer_suppliers WHERE supplier_id = ?",
                id
        );
    }
}
