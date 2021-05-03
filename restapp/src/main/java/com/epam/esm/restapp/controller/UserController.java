package com.epam.esm.restapp.controller;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public PagedModel<EntityModel<UserDTO>> searchUsers(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "search", required = false) String searchParameters,
                                     @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<UserDTO> users = userService.searchUsers(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
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
                linkTo(methodOn(GiftCertificatesController.class).findAll()).withRel("users"));
    }

    @GetMapping
    public PagedModel<EntityModel<UserDTO>> findAll(@RequestParam(value = "page", required = false) Integer page,
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

}
