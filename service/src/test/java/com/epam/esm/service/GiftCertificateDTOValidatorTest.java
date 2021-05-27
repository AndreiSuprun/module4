//package com.epam.esm.service;
//
//import com.epam.esm.dao.GiftCertificateDAO;
//import com.epam.esm.entity.GiftCertificate;
//import com.epam.esm.entity.Tag;
//import com.epam.esm.service.dto.GiftCertificateDTO;
//import com.epam.esm.service.dto.PaginationDTO;
//import com.epam.esm.service.dto.TagDTO;
//import com.epam.esm.service.exception.ValidationException;
//import com.epam.esm.service.impl.GiftCertificatesServiceImpl;
//import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
//import com.epam.esm.service.validator.GiftCertificateDTOValidator;
//import com.epam.esm.service.validator.impl.GiftCertificateValidator;
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//public class GiftCertificateDTOValidatorTest {
//
//    @InjectMocks
//    private GiftCertificateDTOValidator certificateDTOValidator;
//    @Mock
//    private GiftCertificateDAO giftCertificateDAO;
//
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void updateCertificateCorrectTest() {
//        Long id = 1L;
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Certificate");
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Certificate");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Certificate");
//
//        when(giftCertificateDAO.findOne(id)).thenReturn(certificateInDB);
//        when(giftCertificateDAO.findByName(expectedDTO.getName())).thenReturn(null);
//
//        GiftCertificate actual = certificateDTOValidator.validate(expectedDTO, id);
//
//        assertEquals(certificateInDB, actual);
//        verify(giftCertificateDAO, times(1)).findOne(id);
//        verify(giftCertificateDAO, times(1)).findByName(expectedDTO.getName());
//    }
//
//    @Test
//    void updateCertificateNotFoundTest() {
//        Long id = 1L;
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Certificate");
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Certificate");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Certificate");
//
//        doThrow(ValidationException.class).when(giftCertificateDAO).findOne(id);
//        when(giftCertificateDAO.findByName(expectedDTO.getName())).thenReturn(null);
//
//        assertThrows(ValidationException.class, () -> certificateDTOValidator.validate(expectedDTO, id));
//
//        verify(giftCertificateDAO, times(1)).findOne(id);
//        verify(giftCertificateDAO, never()).findByName(expectedDTO.getName());
//    }
//
//    @Test
//    void updateCertificateAlreadyInDBTest() {
//        Long id = 1L;
//        GiftCertificate certificateInDB = new GiftCertificate();
//        certificateInDB.setName("Certificate");
//        GiftCertificateDTO expectedDTO = new GiftCertificateDTO();
//        expectedDTO.setName("Certificate");
//        GiftCertificate expected = new GiftCertificate();
//        expected.setName("Certificate");
//
//        when(giftCertificateDAO.findOne(id)).thenReturn(certificateInDB);
//        doThrow(ValidationException.class).when(giftCertificateDAO).findByName(expectedDTO.getName());
//        assertThrows(ValidationException.class, () -> certificateDTOValidator.validate(expectedDTO, id));
//
//        verify(giftCertificateDAO, times(1)).findOne(id);
//        verify(giftCertificateDAO, times(1)).findByName(expectedDTO.getName());
//    }
//}