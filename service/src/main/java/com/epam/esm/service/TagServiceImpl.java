package com.epam.esm.service;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
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
    public Tag find(Long id) {
        Optional<Tag> tagOptional = tagDAO.findOne(id);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, "id", id);
        }
        return tagOptional.get();
    }

    @Override
    public List<Tag> findAll() {
        return tagDAO.findAll();
    }

    @Override
    public Tag findByName(String name) {
        Optional<Tag> tagOptional = tagDAO.findByName(name);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, "name", name);
        }
        return tagOptional.get();
    }

    @Override
    public void delete(Long id) {
        tagDAO.delete(id);
    }
}
