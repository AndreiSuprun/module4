package com.epam.esm.entity;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class OrderItem {

    @ManyToOne
    private GiftCertificate giftCertificate;
    private Integer quantity;

    public OrderItem() {
    }

    public OrderItem(GiftCertificate giftCertificate, Integer quantity) {
        this.giftCertificate = giftCertificate;
        this.quantity = quantity;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
