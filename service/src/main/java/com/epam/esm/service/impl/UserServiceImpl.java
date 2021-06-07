package com.epam.esm.service.impl;

import com.epam.esm.dao.RoleRepository;
import com.epam.esm.dao.UserRepository;
import com.epam.esm.entity.ERole;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.mapper.impl.OrderMapper;
import com.epam.esm.service.mapper.impl.UserMapper;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.service.security.JwtResponse;
import com.epam.esm.service.security.JwtUtils;
import com.epam.esm.service.security.RegisterRequest;
import com.epam.esm.service.security.UserDetailsImpl;
import com.epam.esm.service.validator.impl.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final UserMapper mapper;
    private final OrderMapper orderMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserValidator userValidator, UserMapper mapper, OrderMapper orderMapper, AuthenticationManager authenticationManager, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userValidator = userValidator;
        this.mapper = mapper;
        this.orderMapper = orderMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponse authenticate(String userName, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);
    }

    @Override
    public UserDTO add(RegisterRequest registerRequest){
        userValidator.validate(registerRequest);
        if (userRepository.existsByUserName(registerRequest.getUserName())){
            throw new ValidationException(ErrorCode.USER_ALREADY_EXIST, registerRequest.getUserName());
        }
        User user = new User(registerRequest.getUserName(), registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.USER).get());
        user.setRoles(roles);
        return mapper.mapEntityToDTO(userRepository.save(user));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<UserDTO> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams,
                                     Pageable pageable) {
        Page<User> users = userRepository.findByQuery(searchParams, orderParams, pageable);
        return users.map(mapper::mapEntityToDTO);
    }

    @Override
    public UserDTO find(Long id) {
        Optional<User> user = userRepository.findById(id);
        return mapper.mapEntityToDTO(user.orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, id)));
    }

    @Override
    public UserDTO findByUserName(String name) {
        Optional<User> user = userRepository.findByUserName(name);
        return mapper.mapEntityToDTO(user.orElseThrow(() -> new ValidationException(ErrorCode.USER_NOT_FOUND, name)));
    }
}
