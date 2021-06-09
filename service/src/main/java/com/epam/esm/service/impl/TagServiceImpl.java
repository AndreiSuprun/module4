package com.epam.esm.service.impl;

import com.epam.esm.dao.TagRepository;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.impl.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper mapper;
    private final TagValidator tagValidator;
    private static final String NAME = "name";
    private static final String ID = "id";

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, TagMapper mapper, TagValidator tagValidator) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.tagValidator = tagValidator;
    }

    @Transactional
    @Override
    public TagDTO add(TagDTO tagDTO) {
        Tag tag = mapper.mapDtoToEntity(tagDTO);
        tagValidator.validate(tag);
        if (tagRepository.findByName(tag.getName()).isPresent()){
            throw new ValidationException(ErrorCode.TAG_ALREADY_IN_DB, tag.getName());
        }
        Tag tagInDB = tagRepository.save(mapper.mapDtoToEntity(tagDTO));
        return mapper.mapEntityToDTO(tagInDB);
    }

    @Override
    public TagDTO find(Long id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(tagOptional.get());
    }

    @Override
    public Page<TagDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                        Pageable pageable) {
        Page<Tag> tags = tagRepository.findByQuery(searchParams, orderParams, pageable);
        return tags.map(mapper::mapEntityToDTO);
    }

    @Override
    public TagDTO findByName(String name) {
        Optional<Tag> tagOptional = tagRepository.findByName(name);
        if (!tagOptional.isPresent()){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND, NAME, name);
        }
        return mapper.mapEntityToDTO(tagOptional.get());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!tagRepository.existsById(id)){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND, ID, id);
        }
        if (!tagRepository.getCertificatesForTag(id).isEmpty()){
            throw new ValidationException(ErrorCode.TAG_CANNOT_BE_DELETED, id);
        }
        tagRepository.deleteById(id);
    }

    public TagDTO findMostWidelyUsedTag(){
        Tag tag = tagRepository.findMostWidelyUsedTag();
        if (tag == null){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND);
        }
        return mapper.mapEntityToDTO(tag);
    }
}
