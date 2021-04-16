package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    final static String SELECT_ONE_GIFT_CERTIFICATE = "SELECT * FROM gift_certificates WHERE id=?";
    final static String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificates WHERE id=?";
    final static String INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    final static String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificates SET name = ?, description = ?, price = ?, duration = ?, last_update_time = ? WHERE id =?";
    final static String SELECT_ALL_GIFT_CERTIFICATES = "SELECT * FROM gift_certificates  gs";
    final static String ADD_TAG_TO_GIFT_CERTIFICATE = "INSERT INTO gift_certificate_tags VALUES (?, ?)";
    final static String SQL_SELECT_TAGS = "SELECT * FROM tags t INNER JOIN gift_certificate_tags gt " +
            "ON t.id = gt.tags_id WHERE gt.gift_certificate_id = ?";
    final static String SQL_SELECT_BY_TAG = " INNER JOIN gift_certificate_tags gt ON gs.id = gt.tags_id INNER JOIN tags t ON t.id = gt.tags_id WHERE t.name = ?";
    final static String SQL_QUERY_BY_NAME = " AND WHERE gs.name LIKE %";
    final static String SQL_QUERY_BY_DESCRIPTION = " AND gs.description LIKE %";
    final static String SQL_QUERY_ORDER = " ORDER BY";
    final static String SQL_QUERY_ORDER_BY_DATE = " gs.create_date";
    final static String SQL_QUERY_ORDER_BY_NAME = " gs.name";
    final static String PERCENT_SIGN = "%";
    final static String SPLIT_SIGN = ";";
    final static String TAG = "tag";
    final static String NAME = "name";
    final static String DESCRIPTION = "description";
    final static String SORT = "sort";
    final static String DATE = "date";


    private JdbcTemplate jdbcTemplate;


    private TagDAO tagDAO;

    @Autowired
    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate, TagDAO tagDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagDAO = tagDAO;
    }

    @Override
    public Optional<GiftCertificate> findOne(long id) {
        GiftCertificate giftCertificate = jdbcTemplate.queryForObject(SELECT_ONE_GIFT_CERTIFICATE, new GiftCertificateMapper(), id);
        if (giftCertificate != null) {
            List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAGS, new TagMapper(), id);
            giftCertificate.setTags(tags);
        }
        return Optional.ofNullable(giftCertificate);
}

    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> list = jdbcTemplate.query(SELECT_ALL_GIFT_CERTIFICATES, new GiftCertificateMapper());
        for (GiftCertificate certificate : list) {
            List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAGS, new TagMapper(), certificate.getId());
            certificate.setTags(tags);
        }
        return list;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id);
    }

    @Override
    public GiftCertificate insert(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(INSERT_GIFT_CERTIFICATE);
            preparedStatement.setString(1, certificate.getName());
            preparedStatement.setString(2, certificate.getDescription());
            preparedStatement.setBigDecimal(3, certificate.getPrice());
            preparedStatement.setInt(4, certificate.getDuration());
            preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
            preparedStatement.setDate(6, new Date(System.currentTimeMillis()));
            return preparedStatement;
        }, keyHolder);
        Long id = (Long) keyHolder.getKey();
        certificate.setId(id);

        for (Tag tag : certificate.getTags()) {
            Optional<Tag> tagInDBOpt = tagDAO.findByName(tag.getName());
            if (!tagInDBOpt.isPresent()) {
                tagInDB = tagDAO.insert(tag);
            }
            jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE, id, tagInDB.getId());
        }
        return certificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getId());
        for (Tag tag : certificate.getTags()) {
            Tag tagInDB = tagDAO.findByName(tag.getName());
            if (tagInDB == null) {
                tagInDB = tagDAO.insert(tag);
            }
            jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE, certificate.getId(), tagInDB.getId());
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByQuery(Map<String, String> query) {
        String sqlQuery = SELECT_ALL_GIFT_CERTIFICATES;
        if (query.containsKey(TAG)) {
            sqlQuery = sqlQuery.concat(SQL_SELECT_BY_TAG);
        }
        if (query.containsKey(NAME)) {
            sqlQuery = sqlQuery.concat(SQL_QUERY_BY_NAME + query.get(NAME) + PERCENT_SIGN);
        }
        if (query.containsKey(DESCRIPTION)) {
            sqlQuery = sqlQuery.concat(SQL_QUERY_BY_DESCRIPTION + query.get(DESCRIPTION) + PERCENT_SIGN);
        }
        if (query.containsKey(SORT)) {
            sqlQuery = sqlQuery.concat(SQL_QUERY_ORDER);
            String[] list = query.get(SORT).split(SPLIT_SIGN);
            for (String string : list) {
                if (string != null && string.contains(DATE)) {
                    sqlQuery = sqlQuery.concat(SQL_QUERY_ORDER_BY_DATE);
                } else {
                    sqlQuery = sqlQuery.concat(SQL_QUERY_ORDER_BY_NAME);
                }
                if (list[0].startsWith("-")) {
                    sqlQuery = sqlQuery.concat(" DESC");
                } else {
                    sqlQuery = sqlQuery.concat(" ASC");
                }
            }
        }
        List<GiftCertificate> certificateList = jdbcTemplate.query(sqlQuery, new GiftCertificateMapper());
        for (GiftCertificate certificate : certificateList) {
            List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAGS, new TagMapper(), certificate.getId());
            certificate.setTags(tags);
        }
        return certificateList;
    }
}
