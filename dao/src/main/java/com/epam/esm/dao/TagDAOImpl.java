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

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Tag> findOne(Long id) {
         List<Tag> tags = jdbcTemplate.query(SELECT_ONE_TAG,  new TagMapper(), id);
         if (!tags.isEmpty()) {
             return Optional.of(tags.get(0));
         } else {
             return Optional.empty();
         }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        List<Tag> tags = jdbcTemplate.query(SELECT_ONE_TAG_BY_NAME,  new TagMapper(), name);
        if (!tags.isEmpty()) {
            return Optional.of(tags.get(0));
        } else {
            return Optional.empty();
        }
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
        Long tagId = (Long) keyHolder.getKey();
        tag.setId(tagId);
        return tag;
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_TAG, id) == 1;
    }
}

