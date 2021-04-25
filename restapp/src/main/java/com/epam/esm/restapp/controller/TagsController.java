package com.epam.esm.restapp.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<TagDTO> findAll() {
        return tagService.findAll();
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
    public TagDTO add(@RequestBody TagDTO newTag) {
        return tagService.add(newTag);
    }

    /**
     * Returns TagDTO object for tag with provided id from repository.
     *
     * @param id id of tag to find
     * @throws ProjectException if tag with provided id is not present in repository
     * @return TagDTO object of tag with provided id in repository
     */
    @GetMapping("/{id}")
    public TagDTO find(@PathVariable Long id) {
        return tagService.find(id);
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
