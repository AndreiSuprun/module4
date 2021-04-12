package com.epam.esm;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class TagsController {

    private final TagService tagService;

    public TagsController(@Autowired TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    List<Tag> all() {
        return tagService.findAll();
    }

    @PostMapping("/tags")
    Tag newTag(@Valid @RequestBody Tag newTag) {
        return tagService.add(newTag);
    }

    @GetMapping("/tags/{id}")
    Tag one(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {

        return tagService.find(id).orElseThrow(() -> new NotFoundException("tag.notfound", id));
    }

    @DeleteMapping("/tags/{id}")
    void delete(@PathVariable @Min(value = 1, message = "{id.minvalue}") Long id) {
        tagService.delete(id);
    }
}
