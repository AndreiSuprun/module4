package com.epam.esm.restapp.controller;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<TagDTO> all() {
        return tagService.findAll();
    }

    @PostMapping
    public TagDTO newTag(@RequestBody TagDTO newTag) {
        return tagService.add(newTag);
    }

    @GetMapping("/{id}")
    public TagDTO one(@PathVariable Long id) {
        return tagService.find(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
