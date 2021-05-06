package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.OrderCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDao;

    private UserMapper mapper;
    private OrderMapper orderMapper;

    @Autowired
    public UserServiceImpl(UserDAO userDao, UserMapper mapper, OrderMapper orderMapper) {
        this.userDao = userDao;
        this.mapper = mapper;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<UserDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                     PaginationDTO paginationDTO) {
        checkPagination(paginationDTO);
        List<User> users;
        Long count = userDao.count(searchParams);
        checkPageNumber(paginationDTO, count);
        users = userDao.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
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
