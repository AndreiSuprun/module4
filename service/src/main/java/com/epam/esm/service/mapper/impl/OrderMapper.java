package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper implements Mapper<Order, OrderDTO> {

    private final static String USER = "user";
    private final static String ORDER_ITEMS = "orderItems";

    private final OrderItemMapper orderItemMapper;
    private final UserMapper userMapper;

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper, UserMapper userMapper) {
        this.orderItemMapper = orderItemMapper;
        this.userMapper = userMapper;
    }

    public Order mapDtoToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        order.setId(orderDTO.getId());
        order.setCreateDate(orderDTO.getCreateDate());
        order.setTotalPrice(orderDTO.getTotalPrice());
        if (orderDTO.getUser() != null) {
            User user = userMapper.mapDtoToEntity(orderDTO.getUser());
            order.setUser(user);
        }
        return order;
    }

    public OrderDTO mapEntityToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO, USER, ORDER_ITEMS);
        UserDTO userDTO = userMapper.mapEntityToDTO(order.getUser());
        orderDTO.setUser(userDTO);
        List<OrderItemDTO> orderItemDTOs = order.getOrderCertificates().stream().
                map(orderItemMapper::mapEntityToDTO).collect(Collectors.toList());
        orderItemDTOs.forEach(orderCertificateDTO -> orderCertificateDTO.setOrderDTO(orderDTO));
        orderDTO.setCertificates(orderItemDTOs);
        return orderDTO;
    }
}

