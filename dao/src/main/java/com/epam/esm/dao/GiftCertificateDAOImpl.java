package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private final static String SELECT_ONE_GIFT_CERTIFICATE = "SELECT * FROM gift_certificates WHERE id=?";
    private final static String DELETE_GIFT_CERTIFICATE = "DELETE FROM gift_certificates WHERE id=?";
    private final static String INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificates SET name = ?, description = ?, price = ?, duration = ?, last_update_time = ? WHERE id =?";
    private final static String SELECT_ALL_GIFT_CERTIFICATES = "SELECT * FROM gift_certificates  gs";
    private final static String ADD_TAG_TO_GIFT_CERTIFICATE = "INSERT INTO gift_certificate_tags VALUES (?, ?)";
    private final static String CLEAR_GIFT_CERTIFICATE_TAGS = "DELETE * FROM gift_certificate_tags WHERE gift_certificate_id=?";
    private final static String SQL_SELECT_TAGS = "SELECT * FROM tags t INNER JOIN gift_certificate_tags gt " +
            "ON t.id = gt.tag_id WHERE gt.gift_certificate_id = ?";

    private JdbcTemplate jdbcTemplate;

    private TagDAO tagDAO;

    @Autowired
    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate, TagDAO tagDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagDAO = tagDAO;
    }

    @Override
    public Optional<GiftCertificate> findOne(Long id) {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(SELECT_ONE_GIFT_CERTIFICATE, new GiftCertificateMapper(), id);
        if (!giftCertificates.isEmpty()) {
            List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAGS, new TagMapper(), id);
            giftCertificates.get(0).setTags(tags);
            return Optional.of(giftCertificates.get(0));
        }
        return Optional.empty();
}
    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> list = jdbcTemplate.query(SELECT_ALL_GIFT_CERTIFICATES, new GiftCertificateMapper());
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
                    .prepareStatement(INSERT_GIFT_CERTIFICATE);
            preparedStatement.setString(1, certificate.getName());
            preparedStatement.setString(2, certificate.getDescription());
            preparedStatement.setBigDecimal(3, certificate.getPrice());
            preparedStatement.setInt(4, certificate.getDuration());
            preparedStatement.setObject(5, LocalDateTime.now());
            preparedStatement.setObject(6, LocalDateTime.now());
            return preparedStatement;
        }, keyHolder);
        Long id = (Long) keyHolder.getKey();
        certificate.setId(id);

        for (Tag tag : certificate.getTags()) {
            Optional<Tag> tagInDBOpt = tagDAO.findByName(tag.getName());
            if (!tagInDBOpt.isPresent()) {
                Tag tagInDB = tagDAO.insert(tag);
            }
            jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE, id, tagInDBOpt.get().getId());
        }
        return certificate;
    }

    public void addTag(GiftCertificate giftCertificate, Tag tag){
        jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE, giftCertificate.getId(), tag.getId());
    }

    public List<Tag> getTags(GiftCertificate giftCertificate){
        return jdbcTemplate.query(SQL_SELECT_TAGS, new TagMapper(), giftCertificate.getId());
    }

    public void clearTags(Long giftCertificateId){
        jdbcTemplate.update(CLEAR_GIFT_CERTIFICATE_TAGS, giftCertificateId);
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration(), certificate.getId());
        for (Tag tag : certificate.getTags()) {
            Optional<Tag> tagInDB = tagDAO.findByName(tag.getName());
            if (!tagInDB.isPresent()) {
                tagInDB = Optional.ofNullable(tagDAO.insert(tag));
            }
            jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE, certificate.getId(), tagInDB.get().getId());
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> findByQuery(String sqlQuery, Object[] params) {
        List<GiftCertificate> certificateList = jdbcTemplate.query(sqlQuery, new GiftCertificateMapper(), params);
        for (GiftCertificate certificate : certificateList) {
            List<Tag> tags = jdbcTemplate.query(SQL_SELECT_TAGS, new TagMapper(), certificate.getId());
            certificate.setTags(tags);
        }
       return certificateList;
    }
}
