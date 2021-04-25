package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Query;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.QueryDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.impl.GiftCertificatesServiceImpl;
import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.mapper.impl.QueryMapper;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.QueryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.isA;
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
    private TagService tagService;
    @Mock
    private GiftCertificateMapper mapper;
    @Mock
    private GiftCertificateValidator validator;
    @Mock
    private QueryMapper queryMapper;
    @Mock
    private QueryValidator queryValidator;

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
        doThrow(ProjectException.class).when(validator).validate(expected);

        assertThrows(ProjectException.class, () -> {
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
        when(giftCertificateDAO.findByName(expected.getName())).thenReturn(Optional.of(expected));
        assertThrows(ProjectException.class, () -> {
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
        when(giftCertificateDAO.findByName(expected.getName())).thenReturn(Optional.empty());
        when(giftCertificateDAO.insert(expected)).thenReturn(expected);
        when(tagService.findByName(tagDTO.getName())).thenReturn(tagDTO);
        doNothing().when(validator).validate(expected);
        doNothing().when(giftCertificateDAO).addTag(isA(GiftCertificate.class), isA(Tag.class));
        when(giftCertificateDAO.findOne(expected.getId())).thenReturn(Optional.of(expected));
        when(giftCertificateDAO.getTags(expected)).thenReturn(tags);
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

        doNothing().when(giftCertificateDAO).clearTags(id);
        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        doNothing().when(validator).validate(expected);
        when(giftCertificateDAO.findByName(expected.getName())).thenReturn(Optional.empty());
        when(giftCertificateDAO.update(expected, id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        when(giftCertificateDAO.getTags(expected)).thenReturn(null);
        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(certificateInDB)).thenReturn(Optional.of(expected));
        GiftCertificateDTO actual = giftCertificateService.update(expectedDTO, id);

        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).update(expected, id);
    }

    @Test
    void updateCertificateNotCorrectIdTest() {
        Long id = 2L;
        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
        certificateDTO.setName("CertificateDTO");

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.empty());

        assertThrows(ProjectException.class, () -> {
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

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(certificateInDB));
        when(giftCertificateDAO.findByName(certificate.getName())).thenReturn(Optional.empty());
        doNothing().when(giftCertificateDAO).clearTags(id);
        when(mapper.mapDtoToEntity(certificateDTO)).thenReturn(certificate);
        doThrow(ProjectException.class).when(validator).validate(certificate);

        assertThrows(ProjectException.class, () -> {
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

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(certificate));
        when(giftCertificateDAO.getTags(certificate)).thenReturn(null);
        when(mapper.mapEntityToDTO(certificate)).thenReturn(certificateDTO);
        doNothing().when(giftCertificateDAO).clearTags(id);
        when(giftCertificateDAO.delete(id)).thenReturn(true);
        giftCertificateService.delete(id);

        verify(giftCertificateDAO, times(1)).clearTags(id);
        verify(giftCertificateDAO, times(1)).delete(id);
    }

    @Test
    void deleteCertificateNotCorrectTest() {
        Long id = 1L;

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.empty());
        when(giftCertificateDAO.delete(id)).thenReturn(false);

        assertThrows(ProjectException.class, () -> {
            giftCertificateService.delete(id);
        });
        verify(giftCertificateDAO, times(0)).delete(id);
    }

    @Test
    void findCertificateNotCorrectTest() {
        Long id = 1L;

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.empty());

        assertThrows(ProjectException.class, () -> {
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

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(expected));
        when(giftCertificateDAO.getTags(expected)).thenReturn(any(List.class));
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

        when(giftCertificateDAO.findAll()).thenReturn(listExpected);
        when(giftCertificateDAO.getTags(expected1)).thenReturn(any(List.class));
        when(giftCertificateDAO.getTags(expected2)).thenReturn(any(List.class));
        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
        List<GiftCertificateDTO> actual = giftCertificateService.findAll();

        assertEquals(listDTOExpected, actual);
        verify(giftCertificateDAO, times(1)).findAll();
    }

    @Test
    void findByQueryCorrectTest() {
        Query query = new Query();
        query.setContains("Query");
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setContains("Query");
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

        when(queryMapper.mapDtoToEntity(queryDTO)).thenReturn(query);
        doNothing().when(queryValidator).validate(query);
        when(giftCertificateDAO.findByQuery(query)).thenReturn(listExpected);
        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
        List<GiftCertificateDTO> actual = giftCertificateService.findByQuery(queryDTO);

        assertEquals(listDTOExpected, actual);
        verify(giftCertificateDAO, times(1)).findByQuery(query);
    }

    @Test
    void findByQueryNotValidTest() {
        Query query = new Query();
        query.setContains("Query");
        QueryDTO queryDTO = new QueryDTO();
        queryDTO.setContains("Query");

        when(queryMapper.mapDtoToEntity(queryDTO)).thenReturn(query);
        doThrow(ProjectException.class).when(queryValidator).validate(query);

        assertThrows(ProjectException.class, () -> {
            giftCertificateService.findByQuery(queryDTO);
        });
        verify(giftCertificateDAO, times(0)).findByQuery(query);
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

        doNothing().when(giftCertificateDAO).clearTags(id);
        doNothing().when(validator).validate(expected);
        when(giftCertificateDAO.findByName(expected.getName())).thenReturn(Optional.empty());
        when(giftCertificateDAO.update(certificateInDB, id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(certificateInDB)).thenReturn(Optional.of(expected));
        GiftCertificateDTO actual = giftCertificateService.patch(expectedDTO, id);

        assertEquals(expectedDTO, actual);
        verify(giftCertificateDAO, times(1)).update(certificateInDB, id);
    }

    @Test
    void patchCertificateNotCorrectIdTest() {
        Long id = 1L;
        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
        expectedDTO.setName("Certificate1");

        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.empty());

        assertThrows(ProjectException.class, () -> {
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

        when(giftCertificateDAO.findOne(any(Long.class))).thenReturn(Optional.of(certificateInDB));
        when(giftCertificateDAO.findByName(certificate.getName())).thenReturn(Optional.empty());
        when(giftCertificateDAO.update(certificate, id)).thenReturn(certificate);
        doNothing().when(giftCertificateDAO).clearTags(any(Long.class));
        when(mapper.mapDtoToEntity(certificateDTO)).thenReturn(certificate);
        doThrow(ProjectException.class).when(validator).validate(certificateInDB);

        assertThrows(ProjectException.class, () -> {
            giftCertificateService.patch(certificateDTO, id);
        });
        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class), anyLong());
    }
}