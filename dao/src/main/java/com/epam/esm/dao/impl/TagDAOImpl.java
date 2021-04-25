package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.rowmapper.TagRowMapper;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDAOImpl implements TagDAO {

    final static String SELECT_ONE_TAG = "SELECT * FROM tags WHERE id=?";
    final static String SELECT_ONE_TAG_BY_NAME = "SELECT * FROM tags WHERE name=?";
    final static String DELETE_TAG = "DELETE FROM tags WHERE id=?";
    final static String INSERT_TAG = "INSERT INTO tags (name) VALUES (?)";
    final static String SELECT_ALL_TAGS = "SELECT * FROM tags";
    final static String SELECT_TAG_WITH_CERTIFICATE = "SELECT COUNT(*) FROM gift_certificate_tags " +
            "WHERE tag_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Tag> findOne(Long id) {
        List<Tag> tags = jdbcTemplate.query(SELECT_ONE_TAG, new TagRowMapper(), id);
        if (!tags.isEmpty()) {
            return Optional.of(tags.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        List<Tag> tags = jdbcTemplate.query(SELECT_ONE_TAG_BY_NAME, new TagRowMapper(), name);
        if (!tags.isEmpty()) {
            return Optional.of(tags.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, new TagRowMapper());
    }

    @Override
    public Tag insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        Long tagId = keyHolder.getKey().longValue();
        tag.setId(tagId);
        return tag;
    }

    @Override
    public Tag update(Tag obj, Long id) {
        throw new UnsupportedOperationException();
    }

    private static final class OrderCountHandler implements RowCallbackHandler {
        private Integer result=0;
        Integer getResult(){
            return result;
        }
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            result += rs.getInt(1);
        }
    }

    @Override
    public Integer getCertificateCount(Long id){
        OrderCountHandler handler = new OrderCountHandler();
        jdbcTemplate.query(SELECT_TAG_WITH_CERTIFICATE, handler, id);
        return handler.getResult();
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_TAG, id) == 1;
    }
}

