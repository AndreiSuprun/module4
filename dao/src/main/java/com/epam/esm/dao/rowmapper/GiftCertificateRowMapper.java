package com.epam.esm.dao.rowmapper;

import com.epam.esm.entity.GiftCertificate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    public GiftCertificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setId(resultSet.getLong("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getBigDecimal("price"));
        certificate.setDuration(resultSet.getInt("duration"));
        certificate.setCreateDate((resultSet.getTimestamp("create_date")).toLocalDateTime());
        certificate.setLastUpdateDate((resultSet.getTimestamp("last_update_date")).toLocalDateTime());
        return certificate;
    }
}
