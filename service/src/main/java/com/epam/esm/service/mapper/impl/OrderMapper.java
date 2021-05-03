package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMapper implements Mapper<Order, OrderDTO> {

    private final static String ORDER_ITEMS = "orderItems";

    private final OrderItemMapper orderItemMapper;

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public Order mapDtoToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order, ORDER_ITEMS);
        if (orderDTO.getOrderItemDTOs() != null){
            List<OrderItem> orderItems = orderDTO.getOrderItemDTOs().stream().map(orderItemMapper::mapDtoToEntity).collect(Collectors.toList());
            order.setOrderItems(orderItems);}
        return order;
    }

    public OrderDTO mapEntityToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO, ORDER_ITEMS);
        if (order.getOrderItems() != null) {
            List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream().map(orderItemMapper::mapEntityToDTO).collect(Collectors.toList());
            orderDTO.setOrderItemDTOs(orderItemDTOs);
        }
        return orderDTO;
    }
}

