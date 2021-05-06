package com.epam.esm.service;

import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ProjectException;

import java.util.List;

/**
 * Service class responsible for processing user-related operations
 *
 * @author Andrei Suprun
 */
public interface OrderService extends GenericService<OrderDTO> {

    /**
     * Finds orders for user with provided id.
     *
     * @param orderDTO Order to place in repository
     * @throws ProjectException if user is not found in repository
     * @return List<OrderDTO> List of orders dto of retrived orders
     */
    OrderDTO placeOrder(OrderDTO orderDTO);

    /**
     * Updates gift certificate according to provided dto object.
     *
     * @param orderDTO GiftCertificateDTO object according to which is necessary to update gift certificate
     *                              in repository
     * @param id id of updated gift certificate
     * @throws ProjectException if fields in provided GiftCertificateDTO is not valid or gift certificate with provided
     * id is not present in repository
     * @return GiftCertificateDTO gift certificate dto of updated gift certificate in repository
     */
    OrderDTO update(OrderDTO orderDTO, Long id);

    /**
     * Removes gift certificate with provided id from repository.
     *
     * @param id id of gift certificate to delete from repository
     * @throws ProjectException if gift certificate with provided id is not present in repository
     */
    void delete(Long id);
}
