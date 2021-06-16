import com.epam.esm.dao.TagRepository;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.mapper.impl.TagMapper;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;
    @Mock
    private TagRepository tagDAO;
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
        doThrow(ValidationException.class).when(validator).validate(expected);

        assertThrows(ValidationException.class, () -> {tagService.add(expectedDTO);});
        verify(tagDAO, never()).save(any(Tag.class));
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
        when(tagDAO.save(expected)).thenReturn(expected);
        when(tagDAO.findByName(expected.getName())).thenReturn(Optional.empty());
        TagDTO actual = tagService.add(expectedDTO);

        assertEquals(expectedDTO, actual);
        verify(tagDAO, times(1)).save(any(Tag.class));
    }

    @Test
    void deleteTagCorrectTest() {
        Tag tag = new Tag("tag");
        tag.setId(1L);

        doNothing().when(tagDAO).delete(tag);
        when(tagDAO.existsById(tag.getId())).thenReturn(true);
        tagService.delete(tag.getId());

        verify(tagDAO, never()).delete(tag);
    }

    @Test
    void deleteTagNotCorrectTest() {
        Tag tag = new Tag("tag");
        tag.setId(1L);

        doNothing().when(tagDAO).delete(tag);

        assertThrows(ValidationException.class, () -> {
            tagService.delete(tag.getId());
        });
    }

    @Test
    void findTagNotCorrectTest() {
        Long id = 1L;

        when(tagDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> {
            tagService.find(id);
        });
        verify(tagDAO, times(1)).findById(id);
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

        when(tagDAO.findById(id)).thenReturn(Optional.of(expected));
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        TagDTO actual = tagService.find(id);

        assertEquals(expectedDTO, actual);
        verify(tagDAO, times(1)).findById(id);
    }

    @Test
    void findByQueryTest() {
        Tag expected1 = new Tag();
        expected1.setName("Tag1");
        expected1.setId(1L);
        Tag expected2 = new Tag();
        expected2.setName("Tag2");
        expected2.setId(2L);
        List<Tag> listExpected = new ArrayList<>();
        listExpected.add(expected1);
        listExpected.add(expected2);
        Page<Tag> pageable = new PageImpl<>(listExpected);
        TagDTO expectedDTO1 = new TagDTO();
        expectedDTO1.setName("Tag1");
        expectedDTO1.setId(1L);
        TagDTO expectedDTO2 = new TagDTO();
        expectedDTO2.setName("Tag2");
        expectedDTO2.setId(2L);
        List<TagDTO> listDTOExpected = new ArrayList<>();
        listDTOExpected.add(expectedDTO1);
        listDTOExpected.add(expectedDTO2);
        Page<TagDTO> pageableDTO = new PageImpl<>(listDTOExpected);

        when(tagDAO.findByQuery(null, null, Pageable.unpaged())).thenReturn(pageable);
        when(mapper.mapEntityToDTO(expected1)).thenReturn(expectedDTO1);
        when(mapper.mapEntityToDTO(expected2)).thenReturn(expectedDTO2);
        Page<TagDTO> actual = tagService.findByQuery(null, null, Pageable.unpaged());

        assertEquals(pageableDTO, actual);
        verify(tagDAO, times(1)).findByQuery(null, null, Pageable.unpaged());
    }
}