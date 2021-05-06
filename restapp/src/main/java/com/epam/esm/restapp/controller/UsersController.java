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
        return  getPagedModel(users, paginationDTO, searchParameters, orderParameters);
    }

    @GetMapping("/{id}")
    public EntityModel<UserDTO> findOne(@PathVariable Long id) {
        UserDTO userDTO = userService.find(id);
        return getEntityModel(userDTO);
    }

    private EntityModel<UserDTO> getEntityModel(UserDTO userDTO){
        return EntityModel.of(userDTO, linkTo(methodOn(UsersController.class).findOne(userDTO.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).withRel("users"));
    }

    private PagedModel<EntityModel<UserDTO>> getPagedModel(List<UserDTO> userDTOs, PaginationDTO paginationDTO,
                                                           String searchParameters, String orderParameters){
        List<EntityModel<UserDTO>> entityModels = userDTOs.stream()
                .map(userDTO -> EntityModel.of(userDTO,
                        linkTo(methodOn(UsersController.class).findOne(userDTO.getId())).withSelfRel()))
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
