package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;

import java.util.List;

/**
 * Service class responsible for processing tag-related operations
 *
 * @author Andrei Suprun
 */
public interface TagService extends GenericService<TagDTO>{

    /**
     * Adds tag to repository according to provided dto object.
     *
     * @param tagDTO TagDTO object on basis of which is created new tag in repository
     * @throws ProjectException if fields in provided TagDTO object is not valid or tag with the same name is already
     * in repository
     * @return TagDTO tag dto of created in repository tag
     */
    TagDTO add(TagDTO tagDTO);

    /**
     * Search most used tag for user with highest total cost of orders.
     *
     * @throws ProjectException if such tag was not found in repository
     * @return TagDTO tag dto of most used tag
     */
    TagDTO findMostWidelyUsedTag();

    /**
     * Retrieves TagDTO object for tag with provided name from repository.
     *
     * @param name name of tag to find
     * @throws ProjectException if tag with provided name is not present in repository
     * @return TagDTO object of tag with provided name in repository
     */
    TagDTO findByName(String name);


    /**
     * Removes tag with provided id from repository.
     *
     * @param id id of tag to delete from repository
     * @throws ProjectException if tag with provided id is not present in repository or there are exist gift
     * certificates which consist this tag
     */
    void delete(Long id);
}
