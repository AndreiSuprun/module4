package com.epam.esm.restapp.controller;

import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDTO> searchUsers(@RequestParam(value = "page", required = false) Integer page,
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
                        linkTo(methodOn(UserController.class).getOne(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).searchUsers(page, size, searchParameters, orderParameters)).withRel("users")))
                .collect(Collectors.toList());


    }

    @GetMapping("/{id}")
    public EntityModel<UserDTO> getOne(@PathVariable Long id) {
        UserDTO userDTO = userService.find(id);
        return EntityModel.of(userDTO, linkTo(methodOn(UserController.class).getOne(id)).withSelfRel(),
                linkTo(methodOn(GiftCertificatesController.class).findAll()).withRel("users"));
    }

}
