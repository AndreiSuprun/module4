package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDAO;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.service.security.JwtResponse;
import com.epam.esm.service.security.JwtUtils;
import com.epam.esm.service.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDao;
    private final UserMapper mapper;
    private final OrderMapper orderMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserDAO userDao, UserMapper mapper, OrderMapper orderMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userDao = userDao;
        this.mapper = mapper;
        this.orderMapper = orderMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponse authenticate(String userName, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    @Override
    public List<UserDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                     PaginationDTO paginationDTO) {
        checkPagination(paginationDTO);
        Long count = userDao.count(searchParams);
        checkPageNumber(paginationDTO, count);
        List<User> users = userDao.findByQuery(searchParams, orderParams, paginationDTO.getPage(), paginationDTO.getSize());
        return users.stream().map(mapper::mapEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO find(Long id) {
        User user = userDao.findOne(id);
        if (user == null) {
            throw new ValidationException(ErrorCode.USER_NOT_FOUND, id);
        }
        return mapper.mapEntityToDTO(user);
    }
}
