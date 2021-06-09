package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.security.JwtResponse;
import com.epam.esm.service.security.RegisterRequest;

import java.util.List;

/**
 * Service class responsible for processing user-related operations
 *
 * @author Andrei Suprun
 */
public interface UserService extends GenericService<UserDTO> {

    JwtResponse authenticate(String userName, String password);

    List<User> findAll();

    UserDTO findByUserName(String name);

    UserDTO add(RegisterRequest registerRequest);
}
