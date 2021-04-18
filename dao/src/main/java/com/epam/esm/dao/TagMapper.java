package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {
    public Tag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong("id"));
        tag.setName(resultSet.getString("name"));
        return tag;
    }
}
