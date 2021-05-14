package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for mapping GiftCertificates to/from GiftCertificates DTO
 *
 * @author Andrei Suprun
 */
@Service
public class GiftCertificateMapper implements Mapper<GiftCertificate, GiftCertificateDTO> {

    private final static String CREATED_ON = "createdOn";
    private final static String UPDATED_ON = "updatedOn";
    private final static String CREATE_BY = "createdOn";
    private final static String UPDATED_BY = "updatedOn";
    private final static String TAGS = "tags";

    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificateMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    /**
     * Maps GiftCertificate DTO object to GiftCertificate entity object.
     *
     * @param giftCertificateDTO DTO object for mapping
     * @return GiftCertificate entity object
     */
    public GiftCertificate mapDtoToEntity(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();
        BeanUtils.copyProperties(giftCertificateDTO, giftCertificate, CREATED_ON, CREATE_BY, UPDATED_ON, UPDATED_BY, TAGS);
        if (giftCertificateDTO.getTags() != null){
            List<Tag> tags = giftCertificateDTO.getTags().stream().map(tagMapper::mapDtoToEntity).collect(Collectors.toList());
            giftCertificate.setTags(tags);}
        return giftCertificate;
    }

    /**
     * Maps GiftCertificate entity object to GiftCertificate DTO object.
     *
     * @param giftCertificate entity object for mapping
     * @return GiftCertificateDTO DTO object
     */
    public GiftCertificateDTO mapEntityToDTO(GiftCertificate giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        BeanUtils.copyProperties(giftCertificate, giftCertificateDTO, CREATED_ON, CREATE_BY, UPDATED_ON, UPDATED_BY, TAGS);
        if (giftCertificate.getTags() != null) {
            List<TagDTO> tagsDTO = giftCertificate.getTags().stream().map(tagMapper::mapEntityToDTO).collect(Collectors.toList());
            giftCertificateDTO.setTags(tagsDTO);
        }
        return giftCertificateDTO;
    }
}

