package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(rs.getInt("id"));
        certificate.setName(rs.getString("name"));
        certificate.setDescription(rs.getString("description"));
        certificate.setPrice(rs.getBigDecimal("price"));
        certificate.setCreateDate(rs.getObject("create_date", LocalDateTime.class));
        certificate.setLastUpdateDate(rs.getObject("last_update_date", LocalDateTime.class));
        return certificate;
    }
}
