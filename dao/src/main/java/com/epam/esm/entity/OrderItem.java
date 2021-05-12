package com.epam.esm.entity;

import com.epam.esm.dao.audit.Audit;
import com.epam.esm.dao.audit.AuditListener;
import com.epam.esm.dao.audit.Auditable;

import javax.persistence.*;

@Entity
@Table(name="orders_certificates")
@EntityListeners(AuditListener.class)
public class OrderItem implements Auditable {

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
    @Embedded
    private Audit audit;

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

    @Override
    public Audit getAudit() {
        return audit;
    }

    @Override
    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem that = (OrderItem) o;

        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        if (certificate != null ? !certificate.equals(that.certificate) : that.certificate != null) return false;
        return quantity != null ? quantity.equals(that.quantity) : that.quantity == null;
    }

    @Override
    public int hashCode() {
        int result = order != null ? order.hashCode() : 0;
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
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
