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

@Service
public class GiftCertificateMapper implements Mapper<GiftCertificate, GiftCertificateDTO> {

    private final static String CREATE_DATE = "createDate";
    private final static String LAS_UPDATE_DATE = "lastUpdateDate";
    private final static String TAGS = "tags";

    private final TagMapper tagMapper;

    @Autowired
    public GiftCertificateMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    public GiftCertificate mapDtoToEntity(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();
        BeanUtils.copyProperties(giftCertificateDTO, giftCertificate, CREATE_DATE, LAS_UPDATE_DATE, TAGS);
        if (giftCertificateDTO.getTags() != null){
            List<Tag> tags = giftCertificateDTO.getTags().stream().map(tagMapper::mapDtoToEntity).collect(Collectors.toList());
            giftCertificate.setTags(tags);}
        return giftCertificate;
    }

    public GiftCertificateDTO mapEntityToDTO(GiftCertificate giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        BeanUtils.copyProperties(giftCertificate, giftCertificateDTO, TAGS);
        if (giftCertificate.getTags() != null) {
            List<TagDTO> tagsDTO = giftCertificate.getTags().stream().map(tagMapper::mapEntityToDTO).collect(Collectors.toList());
            giftCertificateDTO.setTags(tagsDTO);
        }
        return giftCertificateDTO;
    }
}

