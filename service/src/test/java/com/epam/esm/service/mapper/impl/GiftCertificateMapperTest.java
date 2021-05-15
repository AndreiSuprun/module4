package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GiftCertificateMapperTest {

    @InjectMocks
    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    TagMapper tagMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void mapDtoToEntityTest() {
        Tag tag = new Tag("tag");
        GiftCertificate giftCertificate = new GiftCertificate("name", "description", BigDecimal.valueOf(2), 60, Lists.list(tag));
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("tag");
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("name");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setPrice(BigDecimal.valueOf(2));
        giftCertificateDTO.setDuration(60);
        giftCertificateDTO.setTags(Lists.list(tagDTO));

        when(tagMapper.mapEntityToDTO(tag)).thenReturn(tagDTO);
        GiftCertificateDTO actual = giftCertificateMapper.mapEntityToDTO(giftCertificate);

        assertTrue(Objects.deepEquals(actual, giftCertificateDTO));

    }

    @Test
    void mapEntityToDTOTest() {
        Tag tag = new Tag("tag");
        GiftCertificate giftCertificate = new GiftCertificate("name", "description", BigDecimal.valueOf(2), 60, Lists.list(tag));
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("tag");
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("name");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setPrice(BigDecimal.valueOf(2));
        giftCertificateDTO.setDuration(60);
        giftCertificateDTO.setTags(Lists.list(tagDTO));

        when(tagMapper.mapDtoToEntity(tagDTO)).thenReturn(tag);
        GiftCertificate actual = giftCertificateMapper.mapDtoToEntity(giftCertificateDTO);

        assertTrue(Objects.deepEquals(actual, giftCertificate));
    }
}