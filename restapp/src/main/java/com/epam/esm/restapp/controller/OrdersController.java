package com.epam.esm.restapp.controller;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.GiftCertificateDTO;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.search.OrderCriteriaBuilder;
import com.epam.esm.service.search.SearchCriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    @Autowired
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

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
        return getPagedModel(orders, paginationDTO, searchParameters, orderParameters);
    }

    @GetMapping("/{id}")
    public EntityModel<OrderDTO> findOne(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.find(id);
        return getEntityModel(orderDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO) {
        orderDTO = orderService.placeOrder(orderDTO);
        return getEntityModel(orderDTO);
    }

    @PutMapping("/{id}")
    public EntityModel<OrderDTO> update(@RequestBody OrderDTO updatedOrderDTO, @PathVariable Long id) {
        OrderDTO orderDTO = orderService.update(updatedOrderDTO, id);
        return getEntityModel(orderDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<OrderDTO> getEntityModel(OrderDTO order){
        return EntityModel.of(order, linkTo(methodOn(OrdersController.class).findOne(order.getId())).withSelfRel(),
                linkTo(methodOn(OrdersController.class).placeOrder(order)).withRel("place_order"),
                linkTo(methodOn(OrdersController.class).update(order, order.getId())).withRel("update"),
                linkTo(methodOn(OrdersController.class).delete(order.getId())).withRel("delete"),
                linkTo(methodOn(OrdersController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("users"));
    }

    private PagedModel<EntityModel<OrderDTO>> getPagedModel(List<OrderDTO> orders, PaginationDTO pagination,
                                                           String searchParameters, String orderParameters){
        List<EntityModel<OrderDTO>> entityModels = orders.stream()
                .map(order -> EntityModel.of(order,
                        linkTo(methodOn(OrdersController.class).findOne(order.getId())).withSelfRel(),
                        linkTo(methodOn(OrdersController.class).placeOrder(order)).withRel("place_order"),
                        linkTo(methodOn(OrdersController.class).update(order, order.getId())).withRel("update"),
                        linkTo(methodOn(OrdersController.class).delete(order.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getPage() - 1 ,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getPage() + 1,
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(pagination.getTotalPages(),
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(),
                pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
