package com.epam.esm.service.dto;

import com.epam.esm.entity.GiftCertificate;

public class OrderItemDTO {

    private GiftCertificateDTO giftCertificateDTO;
    private Integer quantity;

    public OrderItemDTO() {
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
