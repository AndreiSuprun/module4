package com.epam.esm.service;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService{

    private TagDAO tagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }

    @Override
    public Tag add(Tag tag) {
        return tagDAO.insert(tag);
    }

    @Override
    public Optional<Tag> find(long id) {
        return tagDAO.findOne(id);
    }

    @Override
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    @Override
    public Tag findByName(String name) {
        return tagDAO.findByName(name);
    }

    @Override
    public void delete(long id) {
        tagDAO.delete(id);
    }
}
