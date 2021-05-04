package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    private UserMapper mapper;
    private OrderMapper orderMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserMapper mapper, OrderMapper orderMapper) {
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

    @Override
    public List<UserDTO> findAll(PaginationDTO paginationDTO) {
        checkPagination(paginationDTO);
        List<User> users = userDao.findAll(paginationDTO.getPage(), paginationDTO.getSize());
        return users.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO findOrder(Long userId, Long orderId) {
        find(userId);
        Order order = userDao.getOrder(userId, orderId);
        if (order == null) {
            throw new ProjectException(ErrorCode.ORDER_NOT_FOUND, orderId);
        }
        return orderMapper.mapEntityToDTO(order);
    }

    @Override
    public List<OrderDTO> findOrders(Long userId, PaginationDTO paginationDTO) {
        find(userId);
        List<Order> orders = userDao.getOrders(userId);
        return orders.stream().map(orderMapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO placeOrder(Long userId, OrderDTO orderDTO) {
        find(userId);
        BigDecimal totalPrice = orderDTO.getOrderItemDTOs().stream().
                mapToInt(item -> item.getGiftCertificateDTO().getPrice() * item.getQuantity()).sum();
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setCreateDate(LocalDateTime.now());
        User user = userDao.addOrder(userId, orderMapper.mapDtoToEntity(orderDTO));
        return mapper.mapEntityToDTO(user);
    }
}
