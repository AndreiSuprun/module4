package com.epam.esm.service;

import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.exception.ValidationException;

import java.util.List;

/**
 * Service class responsible for processing order-related operations
 *
 * @author Andrei Suprun
 */
public interface OrderService extends GenericService<OrderDTO> {

    /**
     * Finds orders for user with provided id.
     *
     * @param orderDTO Order to place in repository
     * @throws ValidationException if user is not found in repository
     * @return List<OrderDTO> List of orders dto of retrived orders
     */
    OrderDTO createOrder(OrderDTO orderDTO);

    /**
     * Returns OrderDTO object for order with provided id from repository.
     *
     * @param userId id of user to find orders
     * @param paginationDTO DTO for pagination
     * @return List<OrderDTO> object of orders for user with provided id
     * @throws ValidationException if user with provided id is not present in repository
     */
    List<OrderDTO> findByUser(Long userId, PaginationDTO paginationDTO);

    /**
     * Removes gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to delete from repository
     * @throws ValidationException if gift certificate with provided id is not present in repository
     */
    void delete(Long id);
}
