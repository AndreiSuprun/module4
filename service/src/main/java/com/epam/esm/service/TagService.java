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

    TagDTO findByName(String name);

    boolean exist(TagDTO tagDTO);

    void delete(Long id);
}
