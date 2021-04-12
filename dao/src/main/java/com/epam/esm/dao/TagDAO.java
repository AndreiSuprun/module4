package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

public interface TagDAO extends GenericDao<Tag>{

    Tag findByName(String name);
}
