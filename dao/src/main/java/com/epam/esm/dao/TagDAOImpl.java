package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDAOImpl implements TagDAO{

    final static String SELECT_ONE_TAG = "SELECT * FROM tags WHERE id=?";
    final static String SELECT_ONE_TAG_BY_NAME = "SELECT * FROM tags WHERE name=?";
    final static String DELETE_TAG = "DELETE FROM tag WHERE id=?";
    final static String INSERT_TAG = "INSERT INTO tags (name) VALUES (?)";
    final static String SELECT_ALL_TAGS = "SELECT * FROM tags";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Tag> findOne(long id) {
         Tag tag = jdbcTemplate.queryForObject(SELECT_ONE_TAG,  new TagMapper(), id);
         return Optional.ofNullable(tag);
    }

    @Override
    public Tag findByName(String name) {
        return jdbcTemplate.queryForObject(SELECT_ONE_TAG_BY_NAME,  new TagMapper(), name);
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS,  new TagMapper());
    }

    @Override
    public Tag insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_TAG);
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);
        long tagId = (long) keyHolder.getKey();
        tag.setId(tagId);
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }
}

