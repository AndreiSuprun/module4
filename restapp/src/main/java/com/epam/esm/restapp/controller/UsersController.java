package com.epam.esm.restapp.controller;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public PagedModel<EntityModel<UserDTO>> findByQuery(@RequestParam(value = "page", required = false) Long page,
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
                        linkTo(methodOn(UsersController.class).getOne(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata);

    }

    @GetMapping("/{id}")
    public EntityModel<UserDTO> getOne(@PathVariable Long id) {
        UserDTO userDTO = userService.find(id);
        return EntityModel.of(userDTO, linkTo(methodOn(UsersController.class).getOne(id)).withSelfRel());
                //linkTo(methodOn(UsersController.class).getAll(1, 10)).withRel("users"));
    }

//    @GetMapping
//    public PagedModel<EntityModel<UserDTO>> getAll(@RequestParam(value = "page", required = false) Integer page,
//                                                       @RequestParam(value = "size", required = false) Integer size) {
//        PaginationDTO paginationDTO = new PaginationDTO(page, size);
//        List<UserDTO> userDTOs = userService.findAll(paginationDTO);
//        List<EntityModel<UserDTO>> entityModels = userDTOs.stream()
//                .map(userDTO -> EntityModel.of(userDTO,
//                        linkTo(methodOn(UsersController.class).getOne(userDTO.getId())).withSelfRel()))
//                .collect(Collectors.toList());
//        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
//        return PagedModel.of(entityModels, pageMetadata);
//    }

    @GetMapping("/{id}/orders")
    public PagedModel<EntityModel<OrderDTO>> getOrders(@PathVariable Long id, @RequestParam(value = "page", required = false) Long page,
                                       @RequestParam(value = "size", required = false) Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<OrderDTO> orderDTOs = userService.findOrders(id, paginationDTO);
        List<EntityModel<OrderDTO>> entityModels = orderDTOs.stream()
                .map(orderDTO -> EntityModel.of(orderDTO,
                        linkTo(methodOn(UsersController.class).getOrder(id, orderDTO.getId())).withSelfRel()))
                .collect(Collectors.toList());
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public EntityModel<OrderDTO> getOrder(@PathVariable(value = "userId") Long userId, @PathVariable(value = "orderId") Long orderId) {
        OrderDTO orderDTO = userService.findOrder(userId, orderId);
        return EntityModel.of(orderDTO, linkTo(methodOn(UsersController.class).getOrder(userId, orderId)).withSelfRel());
    }

    @PostMapping("/{id}/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> placeOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        UserDTO userDTO = userService.placeOrder(id, orderDTO);
        return EntityModel.of(userDTO, linkTo(methodOn(UsersController.class).getOne(id)).withSelfRel());
                //linkTo(methodOn(UsersController.class).getAll(1, 10)).withRel("user"));
    }

    private EntityModel<UserDTO> getEntityModel(UserDTO userDTO){
        return EntityModel.of(userDTO, linkTo(methodOn(UsersController.class).find(userDTO.getId())).withSelfRel(),
                linkTo(methodOn(OrdersController.class).getOrders(userDTO.getId())).withRel("user's orders"),
                linkTo(methodOn(UsersController.class).
                        findAll(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE)).withRel("users"));
    }

    private PagedModel<EntityModel<UserDTO>> getPagedModel(List<UserDTO> userDTOs, PaginationDTO paginationDTO,
                                                           String searchParameters, String orderParameters){
        List<EntityModel<UserDTO>> entityModels = userDTOs.stream()
                .map(userDTO -> EntityModel.of(userDTO,
                        linkTo(methodOn(UsersController.class).find(userDTO.getId())).withSelfRel(),
                        linkTo(methodOn(OrdersController.class).getOrders(userDTO.getId())).withRel("user's orders")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (paginationDTO.getTotalPages() > 2 && paginationDTO.getPage() > 2){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    paginationDTO.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (paginationDTO.getTotalPages() > 1 && paginationDTO.getPage() > 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(paginationDTO.getPage() - 1 ,
                    paginationDTO.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(UsersController.class).findByQuery(PaginationDTO.FIRST_PAGE, paginationDTO.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (paginationDTO.getTotalPages() > paginationDTO.getPage()){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(paginationDTO.getPage() + 1,
                    paginationDTO.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (paginationDTO.getTotalPages() > paginationDTO.getPage() - 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(paginationDTO.getTotalPages(),
                    paginationDTO.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getSize(),
                paginationDTO.getPage(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
