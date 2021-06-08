package com.epam.esm.restapp.controller;

import com.epam.esm.dao.UserRepository;
import com.epam.esm.entity.ERole;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import com.epam.esm.service.security.JwtResponse;
import com.epam.esm.service.security.JwtUtils;
import com.epam.esm.service.security.LoginRequest;
import com.epam.esm.service.security.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final String SEARCH_BY_USER_ID= "user_id:";
     @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserService userService;
    private final OrderService orderService;
    private final EntityResponseBuilder responseBuilder;

    @Autowired
    public UsersController(UserService userService, OrderService orderService, EntityResponseBuilder responseBuilder) {
        this.userService = userService;
        this.orderService = orderService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwt = userService.authenticate(loginRequest.getUserName(), loginRequest.getPassword());
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/sign_up")
    public EntityModel<UserDTO> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = userService.add(registerRequest);
        return responseBuilder.getUserEntityModel(userDTO);
    }

    /**
     * Retrieves users from repository according to provided request parameters.
     *
     * @param pageable (optional) request parameter for page number and page size
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return PagedModel<EntityModel<UserDTO>> object of users for returned page from repository
     * @throws ValidationException if provided query is not valid or users according to provided query
     *                          are not present in repository
     */
    @GetMapping()
    public PagedModel<EntityModel<UserDTO>> findByQuery(Pageable pageable,
                                                        @RequestParam(value = "search", required = false) String searchParameters,
                                                        @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        Page<UserDTO> users = userService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                pageable);
        return  responseBuilder.getUserPagedModel(users, pageable, searchParameters, orderParameters);
    }

    /**
     * Returns UserDTO object for user with provided id from repository.
     *
     * @param id id of user to find
     * @return EntityModel<UserDTO> object of user with provided id in repository
     * @throws ValidationException if user with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<UserDTO> findOne(@PathVariable Long id) {
        UserDTO userDTO = userService.find(id);
        return responseBuilder.getUserEntityModel(userDTO);
    }

    /**
     * Returns OrderDTO objects for user with provided id from repository.
     *
     * @param id id of user to find orders
     * @return PagedModel<EntityModel<OrderDTO>> object of orderDTO for user with provided id
     * @throws ValidationException if user with provided id is not present in repository
     */
    @GetMapping("/{id}/orders")
    public PagedModel<EntityModel<OrderDTO>> getUserOrders(Pageable pageable,
                                                     @PathVariable Long id) {
        Page<OrderDTO> orders = orderService.findByUser(id, pageable);
        String searchParameter = SEARCH_BY_USER_ID + id;
        return  responseBuilder.getOrderPagedModel(orders, pageable, searchParameter, null);
    }
}
