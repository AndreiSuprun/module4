package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagDAO extends GenericDao<Tag>{

    Optional<Tag> findByName(String name);
}
