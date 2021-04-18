package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;

import java.util.List;
import java.util.Optional;

public interface TagService {

    TagDTO add(TagDTO tag);

    TagDTO find(Long id);

    List<TagDTO> findAll();

    TagDTO update(TagDTO tagDTO);

    TagDTO findByName(String name);

    void delete(Long id);
}
