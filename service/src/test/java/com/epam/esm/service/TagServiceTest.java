package com.epam.esm.service;

import com.epam.esm.dao.impl.TagDAOImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagDAOImpl tagDAO;
    @Mock
    private TagMapper mapper;
    @Mock
    private TagValidator validator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addTagNotCorrectTest() {
        TagDTO expectedDTO = new TagDTO();
        expectedDTO.setName("Tag");
        Tag expected = new Tag();
        expected.setName("Tag");

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        doThrow(ProjectException.class).when(validator).validate(expected);

        assertThrows(ProjectException.class, () -> {tagService.add(expectedDTO);});
        verify(tagDAO, never()).insert(any(Tag.class));
    }

    @Test
    void addTagCorrectTest() {
        TagDTO expectedDTO = new TagDTO();
        expectedDTO.setName("Tag");
        Tag expected = new Tag();
        expected.setName("Tag");

        when(mapper.mapDtoToEntity(expectedDTO)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        doNothing().when(validator).validate(expected);
        when(tagDAO.insert(expected)).thenReturn(expected);
        when(tagDAO.findByName(expected.getName())).thenReturn(Optional.empty());
        TagDTO actual = tagService.add(expectedDTO);

        assertEquals(expectedDTO, actual);
        verify(tagDAO, times(1)).insert(any(Tag.class));
    }

    @Test
    void deleteTagCorrectTest() {
        Long id = 1L;

        when(tagDAO.delete(id)).thenReturn(true);
        tagService.delete(id);

        verify(tagDAO, times(1)).delete(id);
    }

    @Test
    void deleteTagNotCorrectTest() {
        Long id = 1L;

        when(tagDAO.delete(id)).thenReturn(false);

        assertThrows(ProjectException.class, () -> {
            tagService.delete(id);
        });
        verify(tagDAO, times(1)).delete(id);
    }

    @Test
    void findTagNotCorrectTest() {
        Long id = 1L;

        when(tagDAO.findOne(id)).thenReturn(Optional.empty());

        assertThrows(ProjectException.class, () -> {
            tagService.find(id);
        });
        verify(tagDAO, times(1)).findOne(id);
    }

    @Test
    void findTagCorrectTest() {
        Long id = 1L;
        TagDTO expectedDTO = new TagDTO();
        expectedDTO.setName("Tag");
        expectedDTO.setId(id);
        Tag expected = new Tag();
        expected.setName("Tag");
        expected.setId(id);

        when(tagDAO.findOne(id)).thenReturn(Optional.of(expected));
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        TagDTO actual = tagService.find(id);

        assertEquals(expectedDTO, actual);
        verify(tagDAO, times(1)).findOne(id);
    }

    @Test
    void findAllTagsTest() {
        Tag expected1 = new Tag();
        expected1.setName("Tag1");
        expected1.setId(1L);
        Tag expected2 = new Tag();
        expected2.setName("Tag2");
        expected2.setId(2L);
        List<Tag> listExpected = new ArrayList<>();
        listExpected.add(expected1);
        listExpected.add(expected2);
        TagDTO expectedDTO1 = new TagDTO();
        expectedDTO1.setName("Tag1");
        expectedDTO1.setId(1L);
        TagDTO expectedDTO2 = new TagDTO();
        expectedDTO2.setName("Tag2");
        expectedDTO2.setId(2L);
        List<TagDTO> listDTOExpected = new ArrayList<>();
        listDTOExpected.add(expectedDTO1);
        listDTOExpected.add(expectedDTO2);

        when(tagDAO.findAll()).thenReturn(listExpected);
        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
        List<TagDTO> actual = tagService.findAll();

        assertEquals(listDTOExpected, actual);
        verify(tagDAO, times(1)).findAll();
    }

    @Test
    void tagExistTest() {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("Tag");
        Tag tag = new Tag();
        tag.setName("Tag");

        when(mapper.mapDtoToEntity(tagDTO)).thenReturn(tag);
        doNothing().when(validator).validate(tag);
        when(tagDAO.findByName(tag.getName())).thenReturn(Optional.of(tag));
        boolean isExist = tagService.exist(tagDTO);

        assertTrue(isExist);
        verify(tagDAO, times(1)).findByName(tag.getName());
    }

    @Test
    void tagNotExistTest() {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("Tag");
        Tag tag = new Tag();
        tag.setName("Tag");

        when(mapper.mapDtoToEntity(tagDTO)).thenReturn(tag);
        doNothing().when(validator).validate(tag);
        when(tagDAO.findByName(tag.getName())).thenReturn(Optional.empty());
        boolean isExist = tagService.exist(tagDTO);

        assertFalse(isExist);
        verify(tagDAO, times(1)).findByName(tag.getName());
    }
}