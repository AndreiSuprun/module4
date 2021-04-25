package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.rowmapper.GiftCertificateRowMapper;
import com.epam.esm.dao.rowmapper.TagRowMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private final static String SELECT_ONE_GIFT_CERTIFICATE = "SELECT * FROM gift_certificates WHERE id=?";
    private final static String SELECT_GIFT_CERTIFICATE_BY_NAME = "SELECT * FROM gift_certificates WHERE name=?";
    private final static String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificates WHERE id=?";
    private final static String INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificates SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id =?";
    private final static String SELECT_ALL_GIFT_CERTIFICATES = "SELECT * FROM gift_certificates  gs";
    private final static String ADD_TAG_TO_GIFT_CERTIFICATE = "INSERT INTO gift_certificate_tags VALUES (?, ?)";
    private final static String CLEAR_GIFT_CERTIFICATE_TAGS = "DELETE FROM gift_certificate_tags WHERE gift_certificate_id=?";
    private final static String SQL_SELECT_TAGS = "SELECT * FROM tags t INNER JOIN gift_certificate_tags gt " +
            "ON t.id = gt.tag_id WHERE gt.gift_certificate_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<GiftCertificate> findOne(Long id) {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(SELECT_ONE_GIFT_CERTIFICATE, new GiftCertificateRowMapper(), id);
        if (!giftCertificates.isEmpty()) {
            return Optional.of(giftCertificates.get(0));
        }
        return Optional.empty();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_NAME,
                new GiftCertificateRowMapper(), name);
        if (!giftCertificates.isEmpty()) {
            return Optional.of(giftCertificates.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> list = jdbcTemplate.query(SELECT_ALL_GIFT_CERTIFICATES, new GiftCertificateRowMapper());
        return list;
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id) == 1;
    }

    @Override
    public GiftCertificate insert(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT_GIFT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, certificate.getName());
            preparedStatement.setString(2, certificate.getDescription());
            preparedStatement.setBigDecimal(3, certificate.getPrice());
            preparedStatement.setInt(4, certificate.getDuration());
            preparedStatement.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            preparedStatement.setTimestamp(6, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            return preparedStatement;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        return findOne(id).get();
    }

    public void addTag(GiftCertificate giftCertificate, Tag tag){
        jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE, giftCertificate.getId(), tag.getId());
    }

    public List<Tag> getTags(GiftCertificate giftCertificate){
        return jdbcTemplate.query(SQL_SELECT_TAGS, new TagRowMapper(), giftCertificate.getId());
    }

    public void clearTags(Long giftCertificateId){
        jdbcTemplate.update(CLEAR_GIFT_CERTIFICATE_TAGS, giftCertificateId);
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate, Long id) {
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), id);
        return findOne(id).get();
    }

    @Override
    public List<GiftCertificate> findByQuery(Query query) {
        return jdbcTemplate.query(query.buildSQLQuery(), new GiftCertificateRowMapper(), query.getQueryParams());
    }
}
