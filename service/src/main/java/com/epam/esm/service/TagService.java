package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

    Tag add(Tag tag);

    Optional<Tag> find(long id);

    List<Tag> findAll();

    Tag findByName(String name);

    void delete(long id);
}
