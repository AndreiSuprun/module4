package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for mapping Tag to/from Tag DTO
 *
 * @author Andrei Suprun
 */
@Service
public class TagMapper implements Mapper<Tag, TagDTO> {

    /**
     * Maps Tag DTO object to Tag entity object.
     *
     * @param tagDTO DTO object for mapping
     * @return Tag entity object
     */
    public Tag mapDtoToEntity(TagDTO tagDTO){
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        return tag;
    }

    /**
     * Maps Tag entity object to Tag DTO object.
     *
     * @param tag entity object for mapping
     * @return Tag DTO object
     */
    public TagDTO mapEntityToDTO(Tag tag){
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }
}
