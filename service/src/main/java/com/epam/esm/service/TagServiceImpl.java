package com.epam.esm.service;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.Mapper;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.validator.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService{

    private TagDAO tagDAO;
    private Mapper mapper;
    private TagValidator tagValidator;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, Mapper mapper, TagValidator tagValidator) {
        this.tagDAO = tagDAO;
        this.mapper = mapper;
        this.tagValidator = tagValidator;
    }

    @Override
    public TagDTO add(TagDTO tagDTO) {
        List<String> invalidFields = tagValidator.validate(tagDTO);
        if (!invalidFields.isEmpty()){
            throw new ProjectException(ErrorCode.TAG_FIELD_INVALID, tagDTO.getName());
        }
        if (tagDAO.findByName(tagDTO.getName()).isPresent()){
            throw new ProjectException(ErrorCode.TAG_ALREADY_IN_DB, tagDTO.getName());
        }
        Tag tagInDB = tagDAO.insert(mapper.mapTagDTOToEntity(tagDTO));
        return mapper.mapTagEntityToDTO(tagInDB);
    }

    @Override
    public TagDTO find(Long id) {
        Optional<Tag> tagOptional = tagDAO.findOne(id);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, id);
        }
        return mapper.mapTagEntityToDTO(tagOptional.get());
    }

    @Override
    public List<TagDTO> findAll() {
        List<Tag> tags = tagDAO.findAll();
        return tags.stream().map(mapper::mapTagEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public TagDTO update(TagDTO tagDTO) {
            throw new ProjectException(ErrorCode.TAG_UPDATE_NOT_ALLOWED);
    }

    @Override
    public TagDTO findByName(String name) {
        Optional<Tag> tagOptional = tagDAO.findByName(name);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, "name", name);
        }
        return tagOptional.map(mapper::mapTagEntityToDTO).get();
    }

    @Override
    public void delete(Long id) {
        tagDAO.delete(id);
    }
}
