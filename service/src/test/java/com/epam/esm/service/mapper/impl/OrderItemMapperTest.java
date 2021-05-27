package com.epam.esm.service.mapper.impl;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class OrderItemMapperTest {

    @InjectMocks
    OrderItemMapper orderItemMapper;

    @Mock
    GiftCertificateMapper giftCertificateMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void mapDtoToEntityTest() {
        Tag tag = new Tag("tag");
        GiftCertificate giftCertificate = new GiftCertificate("name", "description", BigDecimal.valueOf(2), 60, Lists.list(tag));
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(giftCertificate);
        orderItem.setQuantity(1);

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

        when(giftCertificateMapper.mapDtoToEntity(giftCertificateDTO)).thenReturn(giftCertificate);

        OrderItem actual = orderItemMapper.mapDtoToEntity(orderItemDTO);

        assertTrue(Objects.deepEquals(actual, orderItem));
    }

    @Test
    void mapEntityToDTOTest() {

        Order order = new Order();
        User user = new User("firstName", "lastName", "email");
        order.setUser(user);
        order.setTotalPrice(BigDecimal.valueOf(2));
        Tag tag = new Tag("tag");
        GiftCertificate giftCertificate = new GiftCertificate("name", "description", BigDecimal.valueOf(2), 60, Lists.list(tag));
        OrderItem orderItem = new OrderItem();
        orderItem.setCertificate(giftCertificate);
        orderItem.setQuantity(1);
        order.setOrderCertificates(Lists.list(orderItem));
        orderItem.setOrder(order);

        OrderDTO orderDTO = new OrderDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("firstName");
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

        when(giftCertificateMapper.mapEntityToDTO(giftCertificate)).thenReturn(giftCertificateDTO);

        OrderItemDTO actual = orderItemMapper.mapEntityToDTO(orderItem);

        assertFalse(Objects.deepEquals(actual, orderItemDTO));
    }
}