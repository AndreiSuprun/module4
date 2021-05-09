package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

public class OrderItemDTO {

    @JsonBackReference
    private OrderDTO orderDTO;
    private GiftCertificateDTO giftCertificateDTO;
    private Integer quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderDTO orderDTO, GiftCertificateDTO giftCertificateDTO, Integer quantity) {
        this.orderDTO = orderDTO;
        this.giftCertificateDTO = giftCertificateDTO;
        this.quantity = quantity;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public GiftCertificateDTO getGiftCertificateDTO() {
        return giftCertificateDTO;
    }

    public void setGiftCertificateDTO(GiftCertificateDTO giftCertificateDTO) {
        this.giftCertificateDTO = giftCertificateDTO;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
