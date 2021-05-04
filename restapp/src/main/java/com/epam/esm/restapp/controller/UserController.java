package com.epam.esm.restapp.controller;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public PagedModel<EntityModel<UserDTO>> findByQuery(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "search", required = false) String searchParameters,
                                     @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<UserDTO> users = userService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                paginationDTO);
        List<EntityModel<UserDTO>> entityModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getOne(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata);

    }

    @GetMapping("/{id}")
    public EntityModel<UserDTO> getOne(@PathVariable Long id) {
        UserDTO userDTO = userService.find(id);
        return EntityModel.of(userDTO, linkTo(methodOn(UserController.class).getOne(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).findAll(1, 10)).withRel("users"));
    }

    @GetMapping
    public PagedModel<EntityModel<UserDTO>> getAll(@RequestParam(value = "page", required = false) Integer page,
                                                       @RequestParam(value = "size", required = false) Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<UserDTO> userDTOs = userService.findAll(paginationDTO);
        List<EntityModel<UserDTO>> entityModels = userDTOs.stream()
                .map(userDTO -> EntityModel.of(userDTO,
                        linkTo(methodOn(UserController.class).getOne(userDTO.getId())).withSelfRel()))
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata);
    }

    @GetMapping("/{id}/orders")
    public PagedModel<EntityModel<OrderDTO>> getOrders(@PathVariable Long id, @RequestParam(value = "page", required = false) Integer page,
                                       @RequestParam(value = "size", required = false) Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<OrderDTO> orderDTOs = userService.findOrders(id, paginationDTO);
        List<EntityModel<OrderDTO>> entityModels = orderDTOs.stream()
                .map(orderDTO -> EntityModel.of(orderDTO,
                        linkTo(methodOn(UserController.class).getOrder(id, orderDTO.getId())).withSelfRel()))
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public EntityModel<OrderDTO> getOrder(@PathVariable(value = "userId") Long userId, @PathVariable(value = "orderId") Long orderId) {
        OrderDTO orderDTO = userService.findOrder(userId, orderId);
        return EntityModel.of(orderDTO, linkTo(methodOn(UserController.class).getOrder(userId, orderId)).withSelfRel());
    }

    @PostMapping("/{id}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> placeOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        UserDTO userDTO = userService.placeOrder(id, orderDTO);
        return EntityModel.of(userDTO, linkTo(methodOn(UserController.class).getOne(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("user"));
    }
}
