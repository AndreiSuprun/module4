package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;

public interface UserDao extends GenericDao<User>{

    Order getOrder(Long userId, Long orderId);

    List<Order> getOrders(Long userId);

}
