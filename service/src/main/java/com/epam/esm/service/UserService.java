package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;

import java.util.List;

/**
 * Service class responsible for processing user-related operations
 *
 * @author Andrei Suprun
 */
public interface UserService extends GenericService<UserDTO> {

    /**
     * Finds order with provided id for user with provided id.
     *
     * @param userId id of user to find order
     * @param orderId id of order for provided user
     * @throws ProjectException if user or order are not found in repository
     * @return OrderDTO order dto of retrived order
     */
    OrderDTO findOrder(Long userId, Long orderId);

    /**
     * Finds orders for user with provided id.
     *
     * @param userId id of user to find orders
     * @throws ProjectException if user is not found in repository
     * @return List<OrderDTO> List of orders dto of retrived orders
     */
    List<OrderDTO> findOrders(Long userId, PaginationDTO paginationDTO);

    UserDTO placeOrder(Long userId, OrderDTO orderDTO);
}
