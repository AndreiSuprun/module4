package com.epam.esm.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Entity
@Table(name="orders_certificates")
@EntityListeners(AuditingEntityListener.class)
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    @ManyToOne
    @MapsId("order")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("certificate")
    @JoinColumn(name = "certificate_id")
    private GiftCertificate certificate;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;
    @Column(name = "updated_by")
    @LastModifiedBy
    private String modifiedBy;
    @Column(name = "created_on")
    @CreatedDate
    private LocalDateTime createdDate;
    @Column(name = "updated_on")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public OrderItem(){
        id = new OrderItemId();
    }

    public OrderItem(Order order, GiftCertificate certificate, Integer quantity) {
        id = new OrderItemId();
        id.setOrder(order);
        id.setCertificate(certificate);
        this.quantity = quantity;
    }

    @Transient
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        id.setOrder(order);
    }

    @Transient
    public GiftCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(GiftCertificate certificate) {
        this.certificate = certificate;
        id.setCertificate(certificate);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem that = (OrderItem) o;

        if (certificate != null ? !certificate.equals(that.certificate) : that.certificate != null) return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = certificate != null ? certificate.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderCertificate{" +
                "id=" + id +
                ", certificate=" + certificate +
                ", quantity=" + quantity +
                '}';
    }
}
