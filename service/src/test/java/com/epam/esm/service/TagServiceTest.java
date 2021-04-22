//package com.epam.esm.service;
//
//import com.epam.esm.dao.impl.GiftCertificateDAOImpl;
//import com.epam.esm.dao.impl.TagDAOImpl;
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
//import com.epam.esm.service.mapper.impl.TagMapper;
//import com.epam.esm.service.validator.impl.GiftCertificateValidator;
//import com.epam.esm.service.validator.impl.QueryValidator;
//import com.epam.esm.service.validator.impl.TagValidator;
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
//
//@ContextConfiguration(classes = ServiceTestConfig.class)
//@ExtendWith(SpringExtension.class)
//public class TagServiceTest {
//
//    @InjectMocks
//    private TagServiceImpl tagService;
//    @Mock
//    private TagDAOImpl tagDAO;
//    @Mock
//    private TagMapper mapper;
//    @Mock
//    private TagValidator validator;
//
//    @BeforeEach
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void addTagNotCorrectTest() {
//        TagDTO expectedDTO = new TagDTO();
//        expectedDTO.setName("Тэг");
//        Tag expected = new Tag();
//        expected.setName("Тэг");
//        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
//        doThrow(ProjectException.class).when(validator).validate(expected);
//        assertThrows(ProjectException.class, () -> {tagService.add(expectedDTO);});
//        verify(tagDAO, never()).insert(any(Tag.class));
//    }
//
//    @Test
//    void addTagCorrectTest() {
//        TagDTO expectedDTO = new TagDTO();
//        expectedDTO.setName("Тэг");
//        Tag expected = new Tag();
//        expected.setName("Тэг");
//        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
//        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
//        doNothing().when(validator).validate(expected);
//        when(tagDAO.insert(expected)).thenReturn(expected);
//        TagDTO actual = tagService.add(expectedDTO);
//        assertEquals(expectedDTO, actual);
//        verify(tagDAO, times(1)).insert(any(Tag.class));
//    }
//
//
//    @Test
//    void deleteTagCorrectTest() {
//        Long id = 1L;
//        when(tagDAO.delete(id)).thenReturn(true);
//        verify(tagDAO, times(1)).delete(id);
//    }
//
//    @Test
//    void deleteTagNotCorrectTest() {
//        Long id = 1L;
//        when(tagDAO.delete(id)).thenReturn(false);
//        assertThrows(ProjectException.class, () -> {tagService.delete(id);});
//        verify(tagDAO, times(1)).delete(id);
//    }
//
//    @Test
//    void findTagNotCorrectTest() {
//        Long id = 1L;
//        assertThrows(ProjectException.class, () -> {tagService.find(id);});
//        verify(tagDAO, times(1)).findOne(id);
//    }
//
//    @Test
//    void findTagCorrectTest() {
//        Long id = 1L;
//        TagDTO expectedDTO = new TagDTO();
//        expectedDTO.setName("Тэг");
//        expectedDTO.setId(id);
//        Tag expected = new Tag();
//        expected.setName("Тэг");
//        expected.setId(id);
//        when(tagDAO.findOne(id)).thenReturn(Optional.of(expected));
//        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
//        TagDTO actual = tagService.find(id);
//        assertEquals(expectedDTO, actual);
//        verify(tagDAO, times(1)).findOne(id);
//    }
//
//    @Test
//    void findAllTagsTest() {
//        Tag expected1 = new Tag();
//        expected1.setName("Тэг1");
//        expected1.setId(1L);
//        Tag expected2 = new Tag();
//        expected2.setName("Тэг2");
//        expected2.setId(2L);
//        List<Tag> listExpected = new ArrayList<>();
//        listExpected.add(expected1);
//        listExpected.add(expected2);
//        TagDTO expectedDTO1 = new TagDTO();
//        expectedDTO1.setName("Тэг1");
//        expectedDTO1.setId(1L);
//        TagDTO expectedDTO2 = new TagDTO();
//        expectedDTO2.setName("Тэг2");
//        expectedDTO2.setId(2L);
//        List<TagDTO> listDTOExpected = new ArrayList<>();
//        listDTOExpected.add(expectedDTO1);
//        listDTOExpected.add(expectedDTO2);
//        when(tagDAO.findAll()).thenReturn(listExpected);
//        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
//        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
//        List<TagDTO> actual = tagService.findAll();
//        assertEquals(listDTOExpected, actual);
//        verify(tagDAO, times(1)).findAll();
//    }
//}