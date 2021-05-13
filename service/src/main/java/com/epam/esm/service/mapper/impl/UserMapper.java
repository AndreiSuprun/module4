package com.epam.esm.service.mapper.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.mapper.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for mapping User to/from User DTO
 *
 * @author Andrei Suprun
 */
@Service
public class UserMapper implements Mapper<User, UserDTO> {

    /**
     * Maps User DTO object to User entity object.
     *
     * @param userDTO DTO object for mapping
     * @return User entity object
     */
    public User mapDtoToEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    /**
     * Maps User entity object to User DTO object.
     *
     * @param user entity object for mapping
     * @return User DTO object
     */
    public UserDTO mapEntityToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}

