package com.epam.esm.service.mapper;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateMapper implements Mapper<GiftCertificate, GiftCertificateDTO> {

    private final static String CREATE_DATE = "createDate";
    private final static String LAS_UPDATE_DATE = "lastUpdateDate";
    private final static String TAGS = "tags";

    public GiftCertificate mapDtoToEntity(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = new GiftCertificate();
        BeanUtils.copyProperties(giftCertificateDTO, giftCertificate, CREATE_DATE, LAS_UPDATE_DATE, TAGS);
        List<Tag> tags = giftCertificateDTO.getTags().stream().map(this::mapTagDTOToEntity).collect(Collectors.toList());
        giftCertificate.setTags(tags);
        return giftCertificate;
    }

    public GiftCertificateDTO mapEntityToDTO(GiftCertificate giftCertificate) {
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        BeanUtils.copyProperties(giftCertificate, giftCertificateDTO, TAGS);
        List<TagDTO> tagsDTO = giftCertificate.getTags().stream().map(this::mapTagEntityToDTO).collect(Collectors.toList());
        giftCertificateDTO.setTags(tagsDTO);
        return giftCertificateDTO;
    }

    public Tag mapTagDTOToEntity(TagDTO tagDTO){
        Tag tag = new Tag();
        tag.setId(tagDTO.getId());
        tag.setName(tagDTO.getName());
        return tag;
    }

    public TagDTO mapTagEntityToDTO(Tag tag){
        TagDTO tagDTO = new TagDTO();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }
}