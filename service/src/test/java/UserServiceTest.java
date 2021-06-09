import com.epam.esm.dao.UserRepository;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.mapper.impl.UserMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userDAO;
    @Mock
    private UserMapper mapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findUserNotCorrectTest() {
        Long id = 1L;

        when(userDAO.findById(id)).thenReturn(null);

        assertThrows(ValidationException.class, () -> {
            userService.find(id);
        });
        verify(userDAO, times(1)).findById(id);
    }

    @Test
    void findUserCorrectTest() {
        Long id = 1L;
        User expected = new User("UserName", "user@mail.com", "111");
        expected.setId(id);
        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setUserName("UserName");
        expectedDTO.setEmail("user@mail.com");
        expectedDTO.setId(id);

        when(userDAO.findById(id)).thenReturn(Optional.of(expected));
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        UserDTO actual = userService.find(id);

        assertEquals(expectedDTO, actual);
        verify(userDAO, times(1)).findById(id);
    }

    @Test
    void findByQueryTest() {
        Long id = 1L;
        User expected = new User("FirstName", "user@mail.com", "111");
        expected.setId(id);
        Page<User> pageable = new PageImpl<>(Lists.list(expected));
        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setUserName("UserName");
        expectedDTO.setEmail("user@mail.com");
        expectedDTO.setId(id);
        Page<UserDTO> pageableDTO = new PageImpl<>(Lists.list(expectedDTO));

        when(userDAO.findByQuery(null, null, Pageable.unpaged())).thenReturn(pageable);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        Page<UserDTO> actual = userService.findByQuery(null, null, Pageable.unpaged());

        assertEquals(pageableDTO, actual);
        verify(userDAO, times(1)).findByQuery(null, null, Pageable.unpaged());
    }
}