package com.epam.esm.service.dto;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    private Long id;
    private UserDTO user;
    private BigDecimal totalPrice;
    private LocalDateTime createdOn;
    private List<OrderItemDTO> certificates = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }


    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @JsonManagedReference
    public List<OrderItemDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<OrderItemDTO> certificates) {
        this.certificates = certificates;
    }
}
