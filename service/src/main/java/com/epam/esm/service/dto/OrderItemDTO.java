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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItemDTO that = (OrderItemDTO) o;

        if (orderDTO != null ? !orderDTO.equals(that.orderDTO) : that.orderDTO != null) return false;
        if (giftCertificateDTO != null ? !giftCertificateDTO.equals(that.giftCertificateDTO) : that.giftCertificateDTO != null)
            return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = orderDTO != null ? orderDTO.hashCode() : 0;
        result = 31 * result + (giftCertificateDTO != null ? giftCertificateDTO.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }
}
