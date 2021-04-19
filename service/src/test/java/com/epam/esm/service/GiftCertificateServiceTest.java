//package com.epam.esm.service;
//
//import com.epam.esm.dao.GiftCertificateDAOImpl;
//import com.epam.esm.dao.TagDAOImpl;
//
//import com.epam.esm.entity.GiftCertificate;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.service.dto.GiftCertificateDTO;
//import com.epam.esm.service.dto.Mapper;
//import com.epam.esm.service.dto.TagDTO;
//import com.epam.esm.service.exception.ErrorCode;
//import com.epam.esm.service.exception.ProjectException;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//public class GiftCertificateServiceTest {
//
//    @InjectMocks
//    private GiftCertificatesServiceImpl giftCertificateService;
//    @Mock
//    private GiftCertificateDAOImpl giftCertificateDAO;
//    @Mock
//    private TagServiceImpl tagService;
//    @Mock
//    private Mapper mapper;
//
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void addCertificateNotCorrectTest() {
//        GiftCertificateDTO expected = new GiftCertificateDTO();
//        expected.setName("Сертификат");
//        assertThrows(ProjectException.class, () -> {giftCertificateService.add(expected);});
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
//        when(mapper.mapCertificateDTOtoEntity(expectedDTO)).thenReturn(expected);
//        when(mapper.mapCertificateEntityToDTO(expected)).thenReturn(expectedDTO);
//        when(giftCertificateDAO.insert(expected)).thenReturn(expected);
//        when(tagService.findByName(tagDTO.getName())).thenReturn(tagDTO);
//        doNothing().when(giftCertificateDAO).addTag(isA(GiftCertificate.class), isA(Tag.class));
//        GiftCertificateDTO actual = giftCertificateService.add(expectedDTO);
//        assertEquals(expectedDTO, actual);
//        verify(giftCertificateDAO, times(1)).insert(expected);
//    }
//
//    @Test
//    void deleteCertificateTest() {
//        Long id = 1L;
//        when(giftCertificateDAO.delete(id)).thenReturn(true);
//        doNothing().when(giftCertificateDAO).clearTags(isA(Long.class));
//        verify(giftCertificateDAO, times(1)).delete(id);
//        verify(giftCertificateDAO, times(1)).clearTags(id);
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
//        when(mapper.mapCertificateEntityToDTO(expected)).thenReturn(expectedDTO);
//        GiftCertificateDTO actual = giftCertificateService.find(id);
//        assertEquals(expectedDTO, actual);
//        verify(giftCertificateDAO, times(1)).findOne(id);
//    }
//
//    @Test
//    void findAllCertificateTest() {
//
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
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Сертификат");
//        expectedDTO.setId(id);
//        when(giftCertificateDAO.findAll()).thenReturn(listExpected);
//        when(giftCertificateDAO.getTags(expected1)).thenReturn(any(List.class));
//        when(giftCertificateDAO.getTags(expected2)).thenReturn(any(List.class));
//        when(mapper.mapCertificateEntityToDTO(expected1)).thenReturn(expectedDTO1);
//        when(mapper.mapCertificateEntityToDTO(expected2)).thenReturn(expectedDTO2);
//        List<GiftCertificateDTO> actual = giftCertificateService.findAll();
//        assertEquals(listDTOExpected, actual);
//        verify(giftCertificateDAO, times(1)).findAll();
//    }
//}
