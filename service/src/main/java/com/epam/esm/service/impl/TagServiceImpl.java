package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.impl.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    @Override
    public TagDTO add(TagDTO tagDTO) {
        Tag tag = mapper.mapDtoToEntity(tagDTO);
        tagValidator.validate(tag);
        if (tagDAO.findByName(tag.getName()) != null){
            throw new ValidationException(ErrorCode.TAG_ALREADY_IN_DB, tag.getName());
        }
        Tag tagInDB = tagDAO.insert(mapper.mapDtoToEntity(tagDTO));
        return mapper.mapEntityToDTO(tagInDB);
    }

    @Override
    public TagDTO find(Long id) {
        Tag tag = tagDAO.findOne(id);
        if (tag == null){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(tag);
    }

    @Override
    public List<TagDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                    PaginationDTO paginationDTO) {
        checkPagination(paginationDTO);
        Long count = tagDAO.count(searchParams);
        checkPageNumber(paginationDTO, count);
        List<Tag> tags = tagDAO.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
        return tags.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public TagDTO findByName(String name) {
        Tag tag = tagDAO.findByName(name);
        if (tag == null){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND, NAME, name);
        }
        return mapper.mapEntityToDTO(tag);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!tagDAO.getCertificatesForTag(id).isEmpty()){
            throw new ValidationException(ErrorCode.TAG_CANNOT_BE_DELETED, id);
        }
        if (!tagDAO.delete(id)){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND, ID, id);
        }
    }

    public TagDTO findMostWidelyUsedTag(){
        Tag tag = tagDAO.findMostWidelyUsedTag();
        if (tag == null){
            throw new ValidationException(ErrorCode.TAG_NOT_FOUND);
        }
        return mapper.mapEntityToDTO(tag);
    }
}
