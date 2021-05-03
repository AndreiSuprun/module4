package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper implements Mapper<User, UserDTO> {

    private final static String ORDERS = "orders";

    private final OrderMapper orderMapper;

    @Autowired
    public UserMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public User mapDtoToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user, ORDERS);
        if (userDTO.getOrders() != null){
            List<Order> orders = userDTO.getOrders().stream().map(orderMapper::mapDtoToEntity).collect(Collectors.toList());
            user.setOrders(orders);}
        return user;
    }

    public UserDTO mapEntityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO, ORDERS);
        if (user.getOrders() != null) {
            List<OrderDTO> ordersDTO = user.getOrders().stream().map(orderMapper::mapEntityToDTO).collect(Collectors.toList());
            userDTO.setOrders(ordersDTO);
        }
        return userDTO;
    }
}

