package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.impl.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;
    private final TagMapper mapper;
    private final TagValidator tagValidator;
    private static final String NAME = "name";
    private static final String ID = "id";

    @Autowired
    public TagServiceImpl(TagDAO tagDAO, TagMapper mapper, TagValidator tagValidator) {
        this.tagDAO = tagDAO;
        this.mapper = mapper;
        this.tagValidator = tagValidator;
    }

    @Override
    public TagDTO add(TagDTO tagDTO) {
        Tag tag = mapper.mapDtoToEntity(tagDTO);
        tagValidator.validate(tag);
        if (tagDAO.findByName(tag.getName()).isPresent()){
            throw new ProjectException(ErrorCode.TAG_ALREADY_IN_DB, tag.getName());
        }
        Tag tagInDB = tagDAO.insert(mapper.mapDtoToEntity(tagDTO));
        return mapper.mapEntityToDTO(tagInDB);
    }

    @Override
    public TagDTO find(Long id) {
        Optional<Tag> tagOptional = tagDAO.findOne(id);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(tagOptional.get());
    }

    @Override
    public List<TagDTO> findAll() {
        List<Tag> tags = tagDAO.findAll();
        return tags.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public TagDTO findByName(String name) {
        Optional<Tag> tagOptional = tagDAO.findByName(name);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, NAME, name);
        }
        return tagOptional.map(mapper::mapEntityToDTO).get();
    }

    @Override
    public boolean exist(TagDTO tagDTO) {
        Tag tag = mapper.mapDtoToEntity(tagDTO);
        tagValidator.validate(tag);
        return tagDAO.findByName(tag.getName()).isPresent();
    }

    @Override
    public void delete(Long id) {
        if (tagDAO.getCertificateCount(id) != 0){
            throw new ProjectException(ErrorCode.TAG_CANNOT_BE_DELETED, id);
        }
        if (!tagDAO.delete(id)){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, ID, id);
        }
    }
}
