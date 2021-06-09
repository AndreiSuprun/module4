package mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.mapper.impl.TagMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class TagMapperTest {

    @InjectMocks
    private TagMapper tagMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void mapDtoToEntityTest() {
        Tag tag = new Tag("tag");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("tag");
        TagDTO actual = tagMapper.mapEntityToDTO(tag);
        assertTrue(Objects.deepEquals(actual, tagDTO));
    }

    @Test
    void mapEntityToDTOTest() {
        Tag tag = new Tag("tag");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("tag");
        Tag actual = tagMapper.mapDtoToEntity(tagDTO);
        assertTrue(Objects.deepEquals(actual, tag));
    }
}