package com.epam.esm.restapp.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
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
    private final EntityResponseBuilder responseBuilder;

    @Autowired
    public TagsController(TagService tagService, EntityResponseBuilder responseBuilder) {
        this.tagService = tagService;
        this.responseBuilder = responseBuilder;
    }

    /**
     * Retrieves tags from repository according to provided request parameters.
     *
     * @param page (optional) request parameter for page number
     * @param size (optional) request parameter for page size
     * @param searchParameters (optional) request parameter for searching
     * @param orderParameters (optional) request parameter for sorting, ascending or descending
     * @return List<Tag> list of tags from repository according to provided query
     * @throws ValidationException if provided query is not valid or tags according to provided query
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
        return responseBuilder.getTagPagedModel(tagDTOs, paginationDTO, searchParameters, orderParameters);
    }

    /**
     * Adds tag to repository according to request body.
     *
     * @param newTag TagDTO object on basis of which is created new tag in repository
     * @return EntityModel<TagDTO> object of tag dto of created in repository tag
     * @throws ValidationException if fields in provided TagDTO object is not valid or tag with the same name is alredy
     * in repository
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDTO> add(@RequestBody TagDTO newTag) {
        TagDTO tagDTO = tagService.add(newTag);
        return responseBuilder.getTagEntityModel(tagDTO);
    }

    /**
     * Returns TagDTO object for tag with provided id from repository.
     *
     * @param id id of tag to find
     * @return EntityModel<TagDTO> object of tag with provided id in repository
     * @throws ValidationException if tag with provided id is not present in repository
     */
    @GetMapping("/{id}")
    public EntityModel<TagDTO> find(@PathVariable Long id) {
        TagDTO tagDTO = tagService.find(id);
        return responseBuilder.getTagEntityModel(tagDTO);
    }

    /**
     * Removes tag with provided id from repository.
     *
     * @param id id of tag to delete from repository
     * @throws ValidationException if tag with provided id is not present in repository or there are exist gift
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
     * @throws ValidationException if tag not found in in repository
     */
    @GetMapping("/most_used")
    public EntityModel<TagDTO> getMostUsed() {
        TagDTO tagDTO = tagService.findMostWidelyUsedTag();
        return responseBuilder.getTagEntityModel(tagDTO);
    }
}
