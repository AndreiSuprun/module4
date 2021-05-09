package com.epam.esm.restapp.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ProjectException;
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
     * Returns all TagDTO objects of tags from repository.
     *
     * @return list of TagDTO objects of retrieved tags
     */
    @GetMapping
    public PagedModel<EntityModel<TagDTO>> findAll(@RequestParam(value = "page", required = false) Long page,
        @RequestParam(value = "size", required = false) Integer size) {
            PaginationDTO paginationDTO = new PaginationDTO(page, size);
            List<TagDTO> tagDTOs = tagService.findAll(paginationDTO);
            return getPagedModel(tagDTOs, paginationDTO);
    }

    /**
     * Adds tag to repository according to request body.
     *
     * @param newTag TagDTO object on basis of which is created new tag in repository
     * @throws ProjectException if fields in provided TagDTO object is not valid or tag with the same name is alredy
     * in repository
     * @return TagDTO tag dto of created in repository tag
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
     * @throws ProjectException if tag with provided id is not present in repository
     * @return TagDTO object of tag with provided id in repository
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

    private EntityModel<TagDTO> getEntityModel(TagDTO tag){
        return EntityModel.of(tag, linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete"),
                linkTo(methodOn(TagsController.class).
                        findAll(PaginationDTO.FIRST_PAGE, PaginationDTO.DEFAULT_RECORDS_PER_PAGE)).withRel("tags"));
    }

    private PagedModel<EntityModel<TagDTO>> getPagedModel(List<TagDTO> tags, PaginationDTO pagination){
        List<EntityModel<TagDTO>> entityModels = tags.stream()
                .map(tag -> EntityModel.of(tag,
                        linkTo(methodOn(TagsController.class).find(tag.getId())).withSelfRel(),
                        linkTo(methodOn(TagsController.class).add(tag)).withRel("add"),
                        linkTo(methodOn(TagsController.class).delete(tag.getId())).withRel("delete")))
                .collect(Collectors.toList());
        List<Link> links = new ArrayList<>();
        if (pagination.getPage() > 0){
            links.add(linkTo(methodOn(TagsController.class).findAll(PaginationDTO.FIRST_PAGE, pagination.getSize()))
                    .withRel(IanaLinkRelations.FIRST));
        }
        if (pagination.getPage() > 1){
            links.add(linkTo(methodOn(TagsController.class).findAll(pagination.getPage() - 1 , pagination.getSize()))
                    .withRel(IanaLinkRelations.PREV));
        }
        links.add(linkTo(methodOn(TagsController.class).findAll(pagination.getPage(), pagination.getSize()))
                    .withRel(IanaLinkRelations.SELF));
        if (pagination.getTotalPages() > pagination.getPage()){
            links.add(linkTo(methodOn(TagsController.class).findAll(pagination.getPage() + 1, pagination.getSize()))
                    .withRel(IanaLinkRelations.NEXT));
        }
        if (pagination.getTotalPages() > pagination.getPage() - 1){
            links.add(linkTo(methodOn(TagsController.class).findAll(pagination.getTotalPages(), pagination.getSize()))
                    .withRel(IanaLinkRelations.LAST));
        }
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(pagination.getSize(), pagination.getPage(), pagination.getTotalCount());
        return PagedModel.of(entityModels, pageMetadata, links);
    }
}
