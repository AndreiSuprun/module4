package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.impl.GiftCertificatesServiceImpl;
import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.validator.GiftCertificateDTOValidator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GiftCertificateServiceTest {

    @InjectMocks
    private GiftCertificatesServiceImpl giftCertificateService;
    @Mock
    private GiftCertificateDAO giftCertificateDAO;
    @Mock
    private GiftCertificateDTOValidator certificateDTOValidator;
    @Mock
    private TagService tagService;
    @Mock
    private GiftCertificateMapper mapper;
    @Mock
    private GiftCertificateValidator validator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addCertificateNotCorrectTest() {
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        doThrow(ValidationException.class).when(validator).validate(expected);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.add(expectedDTO);
        });
        verify(giftCertificateDAO, never()).insert(any(GiftCertificate.class));
    }

    @Test
    void addCertificateExistsTest() {
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        doNothing().when(validator).validate(expected);
        when(giftCertificateDAO.findByName(expected.getName())).thenReturn(expected);
        assertThrows(ValidationException.class, () -> {
            giftCertificateService.add(expectedDTO);
        });
        verify(giftCertificateDAO, never()).insert(any(GiftCertificate.class));
    }

    @Test
    void addCertificateCorrectTest() {
        Tag tag = new Tag();
        tag.setName("Tag");
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("TagDTO");
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");
        expected.setId(1L);

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        doNothing().when(validator).validate(expected);
        when(giftCertificateDAO.findByName(expected.getName())).thenReturn(null);
        when(giftCertificateDAO.insert(expected)).thenReturn(expected);
        when(tagService.findByName(tagDTO.getName())).thenReturn(tagDTO);
        doNothing().when(validator).validate(expected);
        when(giftCertificateDAO.findOne(expected.getId())).thenReturn(expected);
        GiftCertificateDTO actual = giftCertificateService.add(expectedDTO);

        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).insert(expected);
    }

    @Test
    void updateCertificateCorrectTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        doNothing().when(validator).validate(expected);
        when(certificateDTOValidator.validate(expectedDTO, id)).thenReturn(certificateInDB);
        when(giftCertificateDAO.update(expected, id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        when(giftCertificateDAO.findOne(id)).thenReturn(expected);
        GiftCertificateDTO actual = giftCertificateService.update(expectedDTO, id);

        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).update(expected, id);
    }

    @Test
    void updateCertificateNotCorrectIdTest() {
        Long id = 2L;
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setName("CertificateDTO");

        doThrow(ValidationException.class).when(certificateDTOValidator).validate(certificateDTO, id);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.update(certificateDTO, id);
        });
        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class), anyLong());
    }

    @Test
    void updateCertificateNotValidTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setName("Certificate1");
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate1");

        doThrow(ValidationException.class).when(certificateDTOValidator).validate(certificateDTO, id);
        when(mapper.mapDtoToEntity(certificateDTO)).thenReturn(certificate);
        doThrow(ValidationException.class).when(validator).validate(certificate);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.update(certificateDTO, id);
        });
        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class), anyLong());
    }

    @Test
    void deleteCertificateCorrectTest() {
        Long id = 1L;
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate");
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setName("CertificateDTO");

        when(giftCertificateDAO.findOne(id)).thenReturn(certificate);
        when(mapper.mapEntityToDTO(certificate)).thenReturn(certificateDTO);
        when(giftCertificateDAO.delete(id)).thenReturn(true);
        giftCertificateService.delete(id);

        verify(giftCertificateDAO, times(1)).delete(id);
    }

    @Test
    void deleteCertificateNotCorrectTest() {
        Long id = 2L;

        when(giftCertificateDAO.getOrdersForCertificates(id)).thenReturn(Lists.emptyList());
        when(giftCertificateDAO.delete(id)).thenReturn(false);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.delete(id);
        });
        verify(giftCertificateDAO, times(1)).delete(id);
    }

    @Test
    void findCertificateNotCorrectTest() {
        Long id = 1L;

        when(giftCertificateDAO.findOne(id)).thenReturn(null);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.find(id);
        });
        verify(giftCertificateDAO, times(1)).findOne(id);
    }

    @Test
    void findCertificateCorrectTest() {
        Long id = 1L;
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate");
        expected.setId(id);
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate");
        expectedDTO.setId(id);

        when(giftCertificateDAO.findOne(id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        GiftCertificateDTO actual = giftCertificateService.find(id);

        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).findOne(id);
    }

    @Test
    void findAllCertificateTest() {
        GiftCertificate expected1 = new GiftCertificate();
        expected1.setName("Certificate1");
        expected1.setId(1L);
        GiftCertificate expected2 = new GiftCertificate();
        expected2.setName("Certificate2");
        expected2.setId(2L);
        List<GiftCertificate> listExpected = new ArrayList<>();
        listExpected.add(expected1);
        listExpected.add(expected2);
        GiftCertificateDTO expectedDTO1 = new GiftCertificateDTO();
        expectedDTO1.setName("Certificate1");
        expectedDTO1.setId(1L);
        GiftCertificateDTO expectedDTO2 = new GiftCertificateDTO();
        expectedDTO2.setName("Certificate2");
        expectedDTO2.setId(2L);
        List<GiftCertificateDTO> listDTOExpected = new ArrayList<>();
        listDTOExpected.add(expectedDTO1);
        listDTOExpected.add(expectedDTO2);
        PaginationDTO paginationDTO = new PaginationDTO(1L, 10);

        when(giftCertificateDAO.findByQuery(null, null, paginationDTO.getPage(), paginationDTO.getSize())).thenReturn(listExpected);
        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
        List<GiftCertificateDTO> actual = giftCertificateService.findByQuery(null, null, paginationDTO);

        assertEquals(listDTOExpected, actual);
        verify(giftCertificateDAO, times(1)).findByQuery(null, null, paginationDTO.getPage(), paginationDTO.getSize());
    }

    @Test
    void patchCertificateCorrectTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate1");
        GiftCertificate expected = new GiftCertificate();
        expected.setName("Certificate1");

        doNothing().when(validator).validate(expected);
        when(certificateDTOValidator.validate(expectedDTO, id)).thenReturn(certificateInDB);
        when(giftCertificateDAO.update(certificateInDB, id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        when(giftCertificateDAO.findOne(id)).thenReturn(expected);
        GiftCertificateDTO actual = giftCertificateService.patch(expectedDTO, id);

        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).update(certificateInDB, id);
    }

    @Test
    void patchCertificateNotCorrectIdTest() {
        Long id = 1L;
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate1");

        doThrow(ValidationException.class).when(certificateDTOValidator).validate(expectedDTO, id);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.patch(expectedDTO, id);
        });
        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class), anyLong());
    }

    @Test
    void patchCertificateNotValidTest() {
        Long id = 1L;
        GiftCertificate certificateInDB = new GiftCertificate();
        certificateInDB.setName("Certificate");
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setName("Certificate1");
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Certificate1");

        when(certificateDTOValidator.validate(certificateDTO, id)).thenReturn(certificateInDB);
        when(giftCertificateDAO.update(certificate, id)).thenReturn(certificate);
        when(mapper.mapDtoToEntity(certificateDTO)).thenReturn(certificate);
        doThrow(ValidationException.class).when(validator).validate(certificateInDB);

        assertThrows(ValidationException.class, () -> {
            giftCertificateService.patch(certificateDTO, id);
        });
        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class), anyLong());
    }
}