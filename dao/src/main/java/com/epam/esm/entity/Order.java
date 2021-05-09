package com.epam.esm.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {}

    public Order(User user, List<OrderItem> orderItems, BigDecimal totalPrice, LocalDateTime createDate) {
        this.user = user;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<OrderItem> getOrderCertificates() {
        return orderItems;
    }

    public void setOrderCertificates(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderCertificate(OrderItem orderItem){
        orderItems.add(orderItem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (user != null ? !user.equals(order.user) : order.user != null) return false;
        if (totalPrice != null ? !totalPrice.equals(order.totalPrice) : order.totalPrice != null) return false;
        if (createDate != null ? !createDate.equals(order.createDate) : order.createDate != null) return false;
        return orderItems != null ? orderItems.equals(order.orderItems) : order.orderItems == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (orderItems != null ? orderItems.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", totalPrice=" + totalPrice +
                ", createDate=" + createDate +
                ", orderCertificates=" + orderItems +
                '}';
    }
}

