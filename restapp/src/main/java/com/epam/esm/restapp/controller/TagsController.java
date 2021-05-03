package com.epam.esm.restapp.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.dto.UserDTO;
import com.epam.esm.service.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    private TagService tagService;

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
    public PagedModel<EntityModel<TagDTO>> findAll(@RequestParam(value = "page", required = false) Integer page,
        @RequestParam(value = "size", required = false) Integer size) {
            PaginationDTO paginationDTO = new PaginationDTO(page, size);
            List<TagDTO> tagDTOs = tagService.findAll(paginationDTO);
            List<EntityModel<TagDTO>> entityModels = tagDTOs.stream()
                    .map(tagDTO -> EntityModel.of(tagDTO,
                            linkTo(methodOn(TagsController.class).find(tagDTO.getId())).withSelfRel()))
                    .collect(Collectors.toList());
            PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(paginationDTO.getPage(), paginationDTO.getSize(), paginationDTO.getTotalCount());
            return PagedModel.of(entityModels, pageMetadata);
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
        return EntityModel.of(tagDTO, linkTo(methodOn(TagsController.class).find(tagDTO.getId())).withSelfRel());
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
        return EntityModel.of(tagDTO, linkTo(methodOn(TagsController.class).find(tagDTO.getId())).withSelfRel());
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
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
