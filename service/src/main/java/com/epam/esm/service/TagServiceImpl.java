package com.epam.esm.service;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.TagMapper;
import com.epam.esm.service.validator.impl.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService{

    private final TagDAO tagDAO;
    private final TagMapper mapper;
    private final TagValidator tagValidator;

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
    public TagDTO update(TagDTO tagDTO) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TagDTO findByName(String name) {
        Optional<Tag> tagOptional = tagDAO.findByName(name);
        if (!tagOptional.isPresent()){
            throw new ProjectException(ErrorCode.TAG_NOT_FOUND, "name", name);
        }
        return tagOptional.map(mapper::mapEntityToDTO).get();
    }

    @Override
    public void delete(Long id) {
        tagDAO.delete(id);
    }
}
