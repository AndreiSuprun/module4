package com.epam.esm.service.mapper.impl;

import com.epam.esm.dao.audit.Audit;
import com.epam.esm.entity.*;
import com.epam.esm.service.dto.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class OrderMapperTest {

    @InjectMocks
    private OrderMapper orderMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    OrderItemMapper orderItemMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void mapDtoToEntityTest() {
        Order order = new Order();
        User user = new User("firstName", "lastName", "email");
        order.setUser(user);
        order.setTotalPrice(BigDecimal.valueOf(2));
        Tag tag = new Tag("tag");
        GiftCertificate giftCertificate = new GiftCertificate("name", "description", BigDecimal.valueOf(2), 60, Lists.list(tag));
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(giftCertificate);
        orderItem.setQuantity(1);

        OrderDTO orderDTO = new OrderDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("firstName");
        userDTO.setLastName("lastName");
        userDTO.setEmail("email");
        orderDTO.setUser(userDTO);
        orderDTO.setTotalPrice(BigDecimal.valueOf(2));
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("tag");
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("name");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setPrice(BigDecimal.valueOf(2));
        giftCertificateDTO.setDuration(60);
        giftCertificateDTO.setTags(Lists.list(tagDTO));
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setGiftCertificateDTO(giftCertificateDTO);
        orderItemDTO.setQuantity(1);

        when(userMapper.mapDtoToEntity(userDTO)).thenReturn(user);

        Order actual = orderMapper.mapDtoToEntity(orderDTO);

        assertTrue(Objects.deepEquals(actual, order));
    }

    @Test
    void mapEntityToDTOTest() {

        Order order = new Order();
        User user = new User("firstName", "lastName", "email");
        order.setUser(user);
        order.setTotalPrice(BigDecimal.valueOf(2));
        order.setAudit(new Audit());
        order.getAudit().setCreatedOn(LocalDateTime.now());
        Tag tag = new Tag("tag");
        GiftCertificate giftCertificate = new GiftCertificate("name", "description", BigDecimal.valueOf(2), 60, Lists.list(tag));
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(giftCertificate);
        orderItem.setQuantity(1);
        order.setOrderCertificates(Lists.list(orderItem));
        orderItem.setOrder(order);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCreatedOn(order.getAudit().getCreatedOn());
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("firstName");
        userDTO.setLastName("lastName");
        userDTO.setEmail("email");
        orderDTO.setUser(userDTO);
        orderDTO.setTotalPrice(BigDecimal.valueOf(2));
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("tag");
        GiftCertificateDTO giftCertificateDTO = new GiftCertificateDTO();
        giftCertificateDTO.setName("name");
        giftCertificateDTO.setDescription("description");
        giftCertificateDTO.setPrice(BigDecimal.valueOf(2));
        giftCertificateDTO.setDuration(60);
        giftCertificateDTO.setTags(Lists.list(tagDTO));
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setGiftCertificateDTO(giftCertificateDTO);
        orderItemDTO.setQuantity(1);
        orderDTO.setCertificates(Lists.list(orderItemDTO));
        orderItemDTO.setOrderDTO(orderDTO);

        when(userMapper.mapEntityToDTO(user)).thenReturn(userDTO);
        when(orderItemMapper.mapEntityToDTO(orderItem)).thenReturn(orderItemDTO);

        OrderDTO actual = orderMapper.mapEntityToDTO(order);

        assertTrue(Objects.deepEquals(actual, orderDTO));
    }
}