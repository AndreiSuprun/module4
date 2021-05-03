package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.OrderItemDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemMapper implements Mapper<OrderItem, OrderItemDTO> {

    private final static String GIFT_CERTIFICATE = "giftCertificate";

    private final GiftCertificateMapper giftCertificateMapper;

    @Autowired
    public OrderItemMapper(GiftCertificateMapper giftCertificateMapper) {
        this.giftCertificateMapper = giftCertificateMapper;
    }

    public OrderItem mapDtoToEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemDTO, orderItem, GIFT_CERTIFICATE);
        if (orderItemDTO.getGiftCertificateDTO() != null){
            GiftCertificate giftCertificate = giftCertificateMapper.mapDtoToEntity(orderItemDTO.getGiftCertificateDTO());
            orderItem.setGiftCertificate(giftCertificate);}
        return orderItem;
    }

    public OrderItemDTO mapEntityToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        BeanUtils.copyProperties(orderItem, orderItemDTO, GIFT_CERTIFICATE);
        if (orderItem.getGiftCertificate() != null) {
            GiftCertificateDTO giftCertificateDTO = giftCertificateMapper.mapEntityToDTO(orderItem.getGiftCertificate());
            orderItemDTO.setGiftCertificateDTO(giftCertificateDTO);
        }
        return orderItemDTO;
    }
}

