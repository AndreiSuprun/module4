package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.entity.Tag;
import com.epam.esm.controller.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagsController {

    private TagService tagService;

    @Autowired
    public TagsController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> all() {
        return tagService.findAll();
    }

    @PostMapping
    public Tag newTag(@Valid @RequestBody Tag newTag) {
        return tagService.add(newTag);
    }

    @GetMapping("/{id}")
    public Tag one(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
        return tagService.find(id).orElseThrow(() -> new NotFoundException("tag.notfound", id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
        tagService.delete(id);
    }
}
