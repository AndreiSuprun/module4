package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.service.search.SearchCriteria;
import com.epam.esm.service.search.OrderCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private UserMapper mapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserMapper mapper){
        this.userDao = userDao;
        this.mapper = mapper;
    }

    @Override
    public List<UserDTO> searchUsers(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                     PaginationDTO paginationDTO) {
        if (paginationDTO.getPage() == null || paginationDTO.getPage() <= 0){
            paginationDTO.setPage(PaginationDTO.FIRST_PAGE);
        }
        if (paginationDTO.getSize() == null || paginationDTO.getSize() <= 0){
            paginationDTO.setPage(PaginationDTO.DEFAULT_RECORDS_PER_PAGE);
        }
        List<User> users;
        Long count = userDao.count(searchParams);
        if(((long) (paginationDTO.getPage() - 1) * paginationDTO.getSize()) < count) {
            users = userDao.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
        } else {
            throw new ProjectException(ErrorCode.BAD_REQUEST);
        }
        return users.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO find(Long id) {
        User user = userDao.findOne(id);
        if (user == null) {
            throw new ProjectException(ErrorCode.USER_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(user);
    }

}
