package com.epam.esm.restapp.controller;

import com.epam.esm.service.UserService;
import com.epam.esm.service.dto.OrderDTO;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ProjectException;
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

    /**
     * Retrieves users from repository according to provided request parameters.
     *
     * @param page (optional) request parameter for page number
     * @param size (optional) request parameter for page size
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return PagedModel<EntityModel<UserDTO>> object of users for returned page from repository
     * @throws ProjectException if provided query is not valid or users according to provided query
     *                          are not present in repository
     */
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

    /**
     * Returns UserDTO object for user with provided id from repository.
     *
     * @param id id of user to find
     * @return EntityModel<UserDTO> object of user with provided id in repository
     * @throws ProjectException if user with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<UserDTO> findOne(@PathVariable Long id) {
        UserDTO userDTO = userService.find(id);
        return getEntityModel(userDTO);
    }

    private EntityModel<UserDTO> getEntityModel(UserDTO user){
        return EntityModel.of(user, linkTo(methodOn(UsersController.class).findOne(user.getId())).withSelfRel(),
                linkTo(methodOn(UsersController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).withRel("users"));
    }

    private PagedModel<EntityModel<UserDTO>> getPagedModel(List<UserDTO> users, PaginationDTO pagination,
                                                           String searchParameters, String orderParameters){
        List<EntityModel<UserDTO>> entityModels = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UsersController.class).findOne(user.getId())).withSelfRel()))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(PaginationDTO.FIRST_PAGE,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getPage() - 1 ,
                    pagination.getSize(),searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getPage() + 1,
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(UsersController.class).findByQuery(pagination.getTotalPages(),
                    pagination.getSize(), searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(),
                pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
