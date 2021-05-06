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

    private EntityModel<OrderDTO> getEntityModel(OrderDTO orderDTO){
        return EntityModel.of(orderDTO, linkTo(methodOn(OrdersController.class).findOne(orderDTO.getId())).withSelfRel(),
                linkTo(methodOn(OrdersController.class).placeOrder(orderDTO)).withRel("place_order"),
                linkTo(methodOn(OrdersController.class).update(orderDTO, orderDTO.getId())).withRel("update"),
                linkTo(methodOn(OrdersController.class).delete(orderDTO.getId())).withRel("delete"),
                linkTo(methodOn(OrdersController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("users"));
    }

    private PagedModel<EntityModel<OrderDTO>> getPagedModel(List<OrderDTO> orderDTOs, PaginationDTO paginationDTO,
                                                           String searchParameters, String orderParameters){
        List<EntityModel<OrderDTO>> entityModels = orderDTOs.stream()
                .map(orderDTO -> EntityModel.of(orderDTO,
                        linkTo(methodOn(OrdersController.class).findOne(orderDTO.getId())).withSelfRel(),
                        linkTo(methodOn(OrdersController.class).placeOrder(orderDTO)).withRel("place_order"),
                        linkTo(methodOn(OrdersController.class).update(orderDTO, orderDTO.getId())).withRel("update"),
                        linkTo(methodOn(OrdersController.class).delete(orderDTO.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (paginationDTO.getTotalPages() > 2 && paginationDTO.getPage() > 2){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    paginationDTO.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (paginationDTO.getTotalPages() > 1 && paginationDTO.getPage() > 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(paginationDTO.getPage() - 1 ,
                    paginationDTO.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(OrdersController.class).findByQuery(PaginationDTO.FIRST_PAGE, paginationDTO.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (paginationDTO.getTotalPages() > paginationDTO.getPage()){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(paginationDTO.getPage() + 1,
                    paginationDTO.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (paginationDTO.getTotalPages() > paginationDTO.getPage() - 1){
            links.add(linkTo(methodOn(OrdersController.class).findByQuery(paginationDTO.getTotalPages(),
                    paginationDTO.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getSize(),
                paginationDTO.getPage(), paginationDTO.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
