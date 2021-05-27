package com.epam.esm.restapp.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final String SEARCH_BY_USER_ID= "user_id:";

    private final OrderService orderService;
    private final EntityResponseBuilder responseBuilder;

    @Autowired
    public OrdersController(OrderService orderService, EntityResponseBuilder responseBuilder) {
        this.orderService = orderService;
        this.responseBuilder = responseBuilder;
    }

    /**
     * Retrieves orders from repository according to provided request parameters.
     *
     * @param pageable (optional) request parameter for page number
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return PagedModel<EntityModel<OrderDTO>> object of orders for returned page from repository
     * @throws ValidationException if provided query is not valid or orders according to provided query
     *                          are not present in repository
     */
    @GetMapping()
    public PagedModel<EntityModel<OrderDTO>> findByQuery(Pageable pageable,
                                                         @RequestParam(value = "search", required = false) String searchParameters,
                                                         @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        Page<OrderDTO> orders = orderService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                pageable);
        return responseBuilder.getOrderPagedModel(orders, pageable, searchParameters, orderParameters);
    }

    /**
     * Returns OrderDTO object for order with provided id from repository.
     *
     * @param id id of order to find
     * @return EntityModel<OrderDTO> object of orders for user with provided id
     * @throws ValidationException if order with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<OrderDTO> findOne(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.find(id);
        return responseBuilder.getOrderEntityModel(orderDTO);
    }

    /**
     * Adds order to repository according to provided dto object.
     *
     * @param orderDTO OrderDTO object on basis of which is created new order in repository
     * @return EntityModel<OrderDTO> object of created in repository order
     * @throws ValidationException if fields in provided OrderDTO object is not valid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.createOrder(orderDTO);
        return responseBuilder.getOrderEntityModel(orderDTO);
    }

    /**
     * Returns OrderDTO objects for user with provided id from repository.
     *
     * @param userId id of user to find
     * @return PagedModel<EntityModel<OrderDTO>> object of orderDTO for user with provided id
     * @throws ValidationException if user with provided id is not present in repository
     */
    @GetMapping("/users/{userId}")
    public PagedModel<EntityModel<OrderDTO>> findUserOrders(Pageable pageable,
                                                        @PathVariable Long userId) {
        Page<OrderDTO> orders = orderService.findByUser(userId, pageable);
        String searchParameter = SEARCH_BY_USER_ID + userId;
        return  responseBuilder.getOrderPagedModel(orders, pageable, searchParameter, null);
    }

    /**
     * Removes order with provided id from repository.
     *
     * @param id id of order to delete from repository
     * @throws ValidationException if order with provided id is not present in repository
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
