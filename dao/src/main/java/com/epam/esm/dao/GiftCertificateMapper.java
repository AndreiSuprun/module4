package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    public GiftCertificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getInt("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getBigDecimal("price"));
        certificate.setCreateDate(resultSet.getObject("create_date", LocalDateTime.class));
        certificate.setLastUpdateDate(resultSet.getObject("last_update_date", LocalDateTime.class));
        return certificate;
    }
}
