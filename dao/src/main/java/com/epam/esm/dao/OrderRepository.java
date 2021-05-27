package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

    Optional<Order> findById(Long aLong);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Order save(Order order);

    void deleteById(Long id);
}
