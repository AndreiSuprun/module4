package com.epam.esm.restapp.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
     * @param page (optional) request parameter for page number
     * @param size (optional) request parameter for page size
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return PagedModel<EntityModel<OrderDTO>> object of orders for returned page from repository
     * @throws ValidationException if provided query is not valid or orders according to provided query
     *                          are not present in repository
     */
    @GetMapping()
    public PagedModel<EntityModel<OrderDTO>> findByQuery(@RequestParam(value = "page", required = false) Long page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "search", required = false) String searchParameters,
                                     @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<OrderDTO> orders = orderService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                paginationDTO);
        return responseBuilder.getOrderPagedModel(orders, paginationDTO, searchParameters, orderParameters);
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
    public PagedModel<EntityModel<OrderDTO>> findUserOrders(@RequestParam(value = "page", required = false) Long page,
                                                        @RequestParam(value = "size", required = false) Integer size,
                                                        @PathVariable Long userId) {
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<OrderDTO> orders = orderService.findByUser(userId, paginationDTO);
        String searchParameter = SEARCH_BY_USER_ID + userId;
        return  responseBuilder.getOrderPagedModel(orders, paginationDTO, searchParameter, null);
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
