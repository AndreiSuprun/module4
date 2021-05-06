package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDAO orderDAO;
    private UserService userService;

    private OrderMapper mapper;

    @Autowired
    public OrderServiceImpl(OrderDAO orderDAO, UserService userService, OrderMapper mapper) {
        this.orderDAO = orderDAO;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public List<OrderDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                     PaginationDTO paginationDTO) {
        checkPagination(paginationDTO);
        Long count = orderDAO.count(searchParams);
        checkPageNumber(paginationDTO, count);
        List<Order> orders = orderDAO.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
        return orders.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO find(Long id) {
        Order user = orderDAO.findOne(id);
        if (user == null) {
            throw new ProjectException(ErrorCode.ORDER_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(user);
    }

    @Override
    public OrderDTO placeOrder(OrderDTO orderDTO) {
        userService.find(orderDTO.getUserId());
        BigDecimal totalPrice = BigDecimal.valueOf(orderDTO.getOrderItemDTOs().stream().
                mapToInt(item -> item.getGiftCertificateDTO().getPrice().intValue() * item.getQuantity()).sum());
        orderDTO.setTotalPrice(totalPrice);
        orderDTO.setCreateDate(LocalDateTime.now());
        Order order = orderDAO.insert(mapper.mapDtoToEntity(orderDTO));
        return mapper.mapEntityToDTO(order);
    }

    @Transactional
    @Override
    public OrderDTO update(OrderDTO orderDto, Long id) {
        Order order = orderDAO.findOne(id);
        if (order == null) {
            throw new ProjectException(ErrorCode.ORDER_NOT_FOUND, id);
        }
        Order orderInRequest = mapper.mapDtoToEntity(orderDto);
       // validator.validate(orderInRequest);
        order = orderDAO.update(orderInRequest, id);
        return mapper.mapEntityToDTO(order);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        orderDAO.delete(id);
    }
}
