//package com.epam.esm.service;
//
//import com.epam.esm.dao.GiftCertificateDAO;
//import com.epam.esm.dao.impl.GiftCertificateDAOImpl;
//import com.epam.esm.entity.GiftCertificate;
//import com.epam.esm.entity.Query;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.service.dto.GiftCertificateDTO;
//import com.epam.esm.service.dto.QueryDTO;
//import com.epam.esm.service.dto.TagDTO;
//import com.epam.esm.service.exception.ProjectException;
//import com.epam.esm.service.impl.GiftCertificatesServiceImpl;
//import com.epam.esm.service.impl.TagServiceImpl;
//import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
//import com.epam.esm.service.mapper.impl.QueryMapper;
//import com.epam.esm.service.validator.impl.GiftCertificateValidator;
//import com.epam.esm.service.validator.impl.QueryValidator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//@ContextConfiguration(classes = ServiceTestConfig.class)
//@ExtendWith(SpringExtension.class)
//public class GiftCertificateServiceTest {
//
//    @InjectMocks
//    private GiftCertificatesService giftCertificateService;
//    @Mock
//    private GiftCertificateDAO giftCertificateDAO;
//    @Mock
//    private TagService tagService;
//    @Mock
//    private GiftCertificateMapper mapper;
//    @Mock
//    private GiftCertificateValidator validator;
//    @Mock
//    private QueryMapper queryMapper;
//    @Mock
//    private QueryValidator queryValidator;
//
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void addCertificateNotCorrectTest() {
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Сертификат");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Сертификат");
//        doThrow(ProjectException.class).when(validator).validate(expected);
//        assertThrows(ProjectException.class, () -> {giftCertificateService.add(expectedDTO);});
//        verify(giftCertificateDAO, never()).insert(any(GiftCertificate.class));
//    }
//
//    @Test
//    void addCertificateCorrectTest() {
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Сертификат");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Сертификат");
//        TagDTO tagDTO = new TagDTO();
//        tagDTO.setName("TagDTO");
//        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
//        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
//        when(giftCertificateDAO.insert(expected)).thenReturn(expected);
//        when(tagService.findByName(tagDTO.getName())).thenReturn(tagDTO);
//        doNothing().when(giftCertificateDAO).addTag(isA(GiftCertificate.class), isA(Tag.class));
//        GiftCertificateDTO actual = giftCertificateService.add(expectedDTO);
//        assertEquals(expectedDTO, actual);
//        verify(giftCertificateDAO, times(1)).insert(expected);
//    }
//
//    @Test
//    void updateCertificateCorrectTest() {
//        Long id = 1L;
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Сертификат");
//        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(certificateInDB));
//        doNothing().when(giftCertificateDAO).clearTags(id);
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Сертификат1");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Сертификат1");
//        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
//        doNothing().when(validator).validate(expected);
//        when(giftCertificateDAO.update(expected)).thenReturn(expected);
//        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
//        GiftCertificateDTO actual = giftCertificateService.update(expectedDTO, id);
//        assertEquals(expectedDTO, actual);
//        verify(giftCertificateDAO, times(1)).update(certificateInDB);
//    }
//
//    @Test
//    void updateCertificateNotCorrectIdTest() {
//        when(giftCertificateDAO.findOne(any(Long.class))).thenReturn(Optional.empty());
//        assertThrows(ProjectException.class, () -> {giftCertificateService.update(any(GiftCertificateDTO.class), any(Long.class));});
//        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class));
//    }
//
//    @Test
//    void updateCertificateNotValidTest() {
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Сертификат");
//        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
//        certificateDTO.setName("Сертификат1");
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setName("Сертификат1");
//        when(giftCertificateDAO.findOne(any(Long.class))).thenReturn(Optional.of(certificateInDB));
//        doNothing().when(giftCertificateDAO).clearTags(any(Long.class));
//        when(mapper.mapDtoToEntity(certificateDTO)).thenReturn(certificate);
//        doThrow(ProjectException.class).when(validator).validate(certificate);
//        assertThrows(ProjectException.class, () -> {giftCertificateService.update(any(GiftCertificateDTO.class), any(Long.class));});
//        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class));
//    }
//
//    @Test
//    void deleteCertificateCorrectTest() {
//        Long id = 1L;
//        when(giftCertificateDAO.delete(id)).thenReturn(true);
//        doNothing().when(giftCertificateDAO).clearTags(isA(Long.class));
//        verify(giftCertificateDAO, times(1)).delete(id);
//        verify(giftCertificateDAO, times(1)).clearTags(id);
//    }
//
//    @Test
//    void deleteCertificateNotCorrectTest() {
//        Long id = 1L;
//        when(giftCertificateDAO.delete(id)).thenReturn(false);
//        assertThrows(ProjectException.class, () -> {giftCertificateService.delete(id);});
//        verify(giftCertificateDAO, times(1)).delete(id);
//    }
//
//    @Test
//    void findCertificateNotCorrectTest() {
//        Long id = 1L;
//        assertThrows(ProjectException.class, () -> {giftCertificateService.find(id);});
//        verify(giftCertificateDAO, times(1)).findOne(id);
//    }
//
//    @Test
//    void findCertificateCorrectTest() {
//        Long id = 1L;
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Сертификат");
//        expected.setId(id);
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Сертификат");
//        expectedDTO.setId(id);
//        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(expected));
//        when(giftCertificateDAO.getTags(expected)).thenReturn(any(List.class));
//        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
//        GiftCertificateDTO actual = giftCertificateService.find(id);
//        assertEquals(expectedDTO, actual);
//        verify(giftCertificateDAO, times(1)).findOne(id);
//    }
//
//    @Test
//    void findAllCertificateTest() {
//        GiftCertificate expected1 = new GiftCertificate();
//        expected1.setName("Сертификат1");
//        expected1.setId(1L);
//        GiftCertificate expected2 = new GiftCertificate();
//        expected2.setName("Сертификат2");
//        expected2.setId(2L);
//        List<GiftCertificate> listExpected = new ArrayList<>();
//        listExpected.add(expected1);
//        listExpected.add(expected2);
//        GiftCertificateDTO expectedDTO1 = new GiftCertificateDTO();
//        expectedDTO1.setName("Сертификат1");
//        expectedDTO1.setId(1L);
//        GiftCertificateDTO expectedDTO2 = new GiftCertificateDTO();
//        expectedDTO2.setName("Сертификат2");
//        expectedDTO2.setId(2L);
//        List<GiftCertificateDTO> listDTOExpected = new ArrayList<>();
//        listDTOExpected.add(expectedDTO1);
//        listDTOExpected.add(expectedDTO2);
//        when(giftCertificateDAO.findAll()).thenReturn(listExpected);
//        when(giftCertificateDAO.getTags(expected1)).thenReturn(any(List.class));
//        when(giftCertificateDAO.getTags(expected2)).thenReturn(any(List.class));
//        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
//        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
//        List<GiftCertificateDTO> actual = giftCertificateService.findAll();
//        assertEquals(listDTOExpected, actual);
//        verify(giftCertificateDAO, times(1)).findAll();
//    }
//
//    @Test
//    void findByQueryCorrectTest() {
//        Query query = new Query();
//        query.setName("Query");
//        QueryDTO queryDTO = new QueryDTO();
//        queryDTO.setName("Query");
//        when(queryMapper.mapDtoToEntity(queryDTO)).thenReturn(query);
//        doNothing().when(queryValidator).validate(query);
//        GiftCertificate expected1 = new GiftCertificate();
//        expected1.setName("Сертификат1");
//        expected1.setId(1L);
//        GiftCertificate expected2 = new GiftCertificate();
//        expected2.setName("Сертификат2");
//        expected2.setId(2L);
//        List<GiftCertificate> listExpected = new ArrayList<>();
//        listExpected.add(expected1);
//        listExpected.add(expected2);
//        GiftCertificateDTO expectedDTO1 = new GiftCertificateDTO();
//        expectedDTO1.setName("Сертификат1");
//        expectedDTO1.setId(1L);
//        GiftCertificateDTO expectedDTO2 = new GiftCertificateDTO();
//        expectedDTO2.setName("Сертификат2");
//        expectedDTO2.setId(2L);
//        List<GiftCertificateDTO> listDTOExpected = new ArrayList<>();
//        listDTOExpected.add(expectedDTO1);
//        listDTOExpected.add(expectedDTO2);
//        when(giftCertificateDAO.findByQuery(query)).thenReturn(listExpected);
//        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
//        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
//        List<GiftCertificateDTO> actual = giftCertificateService.findByQuery(queryDTO);
//        assertEquals(listDTOExpected, actual);
//        verify(giftCertificateDAO, times(1)).findByQuery(query);
//    }
//
//    @Test
//    void findByQueryNotValidTest() {
//        Query query = new Query();
//        query.setName("Query");
//        QueryDTO queryDTO = new QueryDTO();
//        queryDTO.setName("Query");
//        when(queryMapper.mapDtoToEntity(queryDTO)).thenReturn(query);
//        doThrow(ProjectException.class).when(queryValidator).validate(query);
//        assertThrows(ProjectException.class, () -> {giftCertificateService.findByQuery(queryDTO);});
//        verify(giftCertificateDAO, times(0)).findByQuery(query);
//    }
//
//    @Test
//    void patchCertificateCorrectTest() {
//        Long id = 1L;
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Сертификат");
//        when(giftCertificateDAO.findOne(id)).thenReturn(Optional.of(certificateInDB));
//        doNothing().when(giftCertificateDAO).clearTags(id);
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Сертификат1");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Сертификат1");
//        doNothing().when(validator).validate(expected);
//        when(giftCertificateDAO.update(expected)).thenReturn(expected);
//        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
//        GiftCertificateDTO actual = giftCertificateService.update(expectedDTO, id);
//        assertEquals(expectedDTO, actual);
//        verify(giftCertificateDAO, times(1)).update(certificateInDB);
//    }
//
//    @Test
//    void patchCertificateNotCorrectIdTest() {
//        when(giftCertificateDAO.findOne(any(Long.class))).thenReturn(Optional.empty());
//        assertThrows(ProjectException.class, () -> {giftCertificateService.patch(any(GiftCertificateDTO.class), any(Long.class));});
//        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class));
//    }
//
//    @Test
//    void patchCertificateNotValidTest() {
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Сертификат");
//        GiftCertificateDTO certificateDTO = new GiftCertificateDTO();
//        certificateDTO.setName("Сертификат1");
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setName("Сертификат1");
//        when(giftCertificateDAO.findOne(any(Long.class))).thenReturn(Optional.of(certificateInDB));
//        doNothing().when(giftCertificateDAO).clearTags(any(Long.class));
//        when(mapper.mapDtoToEntity(certificateDTO)).thenReturn(certificate);
//        doThrow(ProjectException.class).when(validator).validate(certificate);
//        assertThrows(ProjectException.class, () -> {giftCertificateService.patch(any(GiftCertificateDTO.class), any(Long.class));});
//        verify(giftCertificateDAO, never()).update(any(GiftCertificate.class));
//    }
//}