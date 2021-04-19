package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAOImpl;
import com.epam.esm.dao.TagDAOImpl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.Mapper;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GiftCertificateServiceTest {

    @InjectMocks
    private GiftCertificatesServiceImpl giftCertificateService;
    @Mock
    private GiftCertificateDAOImpl giftCertificateDAO;
    @Mock
    private TagServiceImpl tagService;
    @Mock
    private Mapper mapper;

    @BeforeAll
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addCertificateNotCorrectTest() {
        GiftCertificateDTO expected = new GiftCertificateDTO();
        expected.setName("Сертификат");
        assertThrows(ProjectException.class, () -> {giftCertificateService.add(expected);});
        verify(giftCertificateDAO, never()).insert(any(GiftCertificate.class));
    }

    @Test
    void addCertificateCorrectTest() {
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Сертификат");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Сертификат");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("TagDTO");
        when(mapper.mapCertificateDTOtoEntity(expectedDTO)).thenReturn(expected);
        when(mapper.mapCertificateEntityToDTO(expected)).thenReturn(expectedDTO);
        when(giftCertificateDAO.insert(expected)).thenReturn(expected);
        when(tagService.findByName(tagDTO.getName())).thenReturn(tagDTO);
        doNothing().when(giftCertificateDAO).addTag(isA(GiftCertificate.class), isA(Tag.class));
        GiftCertificateDTO actual = giftCertificateService.add(expectedDTO);
        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).insert(expected);
    }

}
