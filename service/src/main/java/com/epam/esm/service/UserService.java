package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.security.JwtResponse;

import java.util.List;

/**
 * Service class responsible for processing user-related operations
 *
 * @author Andrei Suprun
 */
public interface UserService extends GenericService<UserDTO> {

    JwtResponse authenticate(String userName, String password);
}
