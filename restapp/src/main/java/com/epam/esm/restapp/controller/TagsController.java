package com.epam.esm.restapp.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.GiftCertificateDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provide a centralized request handling mechanism to
 * handle all types of requests for tags.
 *
 * @author Andrei Suprun
 */
@RestController
@RequestMapping("/tags")
public class TagsController {

    private final TagService tagService;

    @Autowired
    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Retrieves tags from repository according to provided request parameters.
     *
     * @param page (optional) request parameter for page number
     * @param size (optional) request parameter for page size
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return List<Tag> list of tags from repository according to provided query
     * @throws ProjectException if provided query is not valid or tags according to provided query
     *                          are not present in repository
     */
    @GetMapping
    public PagedModel<EntityModel<TagDTO>> findByQuery(@RequestParam(value = "page", required = false) Long page,
                                                       @RequestParam(value = "size", required = false) Integer size,
                                                       @RequestParam(value = "search", required = false) String searchParameters,
                                                       @RequestParam(value = "order", required = false) String orderParameters) {
        SearchCriteriaBuilder searchCriteriaBuilder = new SearchCriteriaBuilder(searchParameters);
        OrderCriteriaBuilder orderCriteriaBuilder = new OrderCriteriaBuilder(orderParameters);
        PaginationDTO paginationDTO = new PaginationDTO(page, size);
        List<TagDTO> tagDTOs = tagService.findByQuery(searchCriteriaBuilder.build(), orderCriteriaBuilder.build(),
                paginationDTO);
        return getPagedModel(tagDTOs, paginationDTO, searchParameters, orderParameters);
    }

    /**
     * Adds tag to repository according to request body.
     *
     * @param newTag TagDTO object on basis of which is created new tag in repository
     * @return EntityModel<TagDTO> object of tag dto of created in repository tag
     * @throws ProjectException if fields in provided TagDTO object is not valid or tag with the same name is alredy
     * in repository
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDTO> add(@RequestBody TagDTO newTag) {
        TagDTO tagDTO = tagService.add(newTag);
        return getEntityModel(tagDTO);
    }

    /**
     * Returns TagDTO object for tag with provided id from repository.
     *
     * @param id id of tag to find
     * @return EntityModel<TagDTO> object of tag with provided id in repository
     * @throws ProjectException if tag with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<TagDTO> find(@PathVariable Long id) {
        TagDTO tagDTO = tagService.find(id);
        return getEntityModel(tagDTO);
    }

    /**
     * Removes tag with provided id from repository.
     *
     * @param id id of tag to delete from repository
     * @throws ProjectException if tag with provided id is not present in repository or there are exist gift
     * certificates which consist this tag
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns TagDTO object for most widely used tag for user with the highest cost of all orders.
     *
     * @return EntityModel<TagDTO> object  for most widely used tag
     * @throws ProjectException if tag not found in in repository
     */
    @GetMapping("/most_used")
    public EntityModel<TagDTO> getMostUsed() {
        TagDTO tagDTO = tagService.findMostWidelyUsedTag();
        return getEntityModel(tagDTO);
    }

    private EntityModel<TagDTO> getEntityModel(TagDTO tag){
        return EntityModel.of(tag, linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete"),
                linkTo(methodOn(TagsController.class).
                        findByQuery(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE, null, null)).
                        withRel("tags"));
    }

    private PagedModel<EntityModel<TagDTO>> getPagedModel(List<TagDTO> tags, PaginationDTO pagination,
                                                          String searchParameters, String orderParameters){
        List<EntityModel<TagDTO>> entityModels = tags.stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                        linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                        linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(PaginationDTO.FIRST_PAGE, pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getPage() - 1 , pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getPage(), pagination.getSize(),
                searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getPage() + 1, pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(TagsController.class).findByQuery(pagination.getTotalPages(), pagination.getSize(),
                    searchParameters, orderParameters))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(), pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
