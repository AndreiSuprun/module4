package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.impl.GiftCertificatesServiceImpl;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.mapper.impl.QueryMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.QueryValidator;
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
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private UserMapper mapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findUserNotCorrectTest() {
        Long id = 1L;

        when(userDAO.findOne(id)).thenReturn(null);

        assertThrows(ProjectException.class, () -> {
            userService.find(id);
        });
        verify(userDAO, times(1)).findOne(id);
    }

    @Test
    void findUserCorrectTest() {
        Long id = 1L;
        User expected = new User("FirstName", "FirstName", "user@mail.com");
        expected.setId(id);
        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setFirstName("FirstName");
        expectedDTO.setLastName("FirstName");
        expectedDTO.setEmail("user@mail.com");
        expectedDTO.setId(id);

        when(userDAO.findOne(id)).thenReturn(expected);
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        UserDTO actual = userService.find(id);

        assertEquals(expectedDTO, actual);
        verify(userDAO, times(1)).findOne(id);
    }

    @Test
    void findByQueryTest() {
        Long id = 1L;
        User expected = new User("FirstName", "FirstName", "user@mail.com");
        expected.setId(id);
        UserDTO expectedDTO = new UserDTO();
        expectedDTO.setFirstName("FirstName");
        expectedDTO.setLastName("FirstName");
        expectedDTO.setEmail("user@mail.com");
        expectedDTO.setId(id);
        PaginationDTO paginationDTO = new PaginationDTO(1L, 10);

        when(userDAO.findByQuery(null, null, paginationDTO.getPage(), paginationDTO.getSize())).thenReturn(Lists.list(expected));
        when(mapper.mapEntityToDTO(expected)).thenReturn(expectedDTO);
        List<UserDTO> actual = userService.findByQuery(null, null, paginationDTO);

        assertEquals(Lists.list(expectedDTO), actual);
        verify(userDAO, times(1)).findByQuery(null, null, paginationDTO.getPage(), paginationDTO.getSize());
    }
}