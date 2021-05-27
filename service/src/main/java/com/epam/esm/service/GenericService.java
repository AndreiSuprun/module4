package com.epam.esm.service;

import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service class responsible for processing generic operations
 *
 * @author Andrei Suprun
 */
public interface GenericService<T> {

    final String PAGE_PARAMETER = "page";
    final String SIZE_PARAMETER = "size";

    /**
     * Returns DTO object for entity with provided id from repository.
     *
     * @param id id of entity to find
     * @return DTO object of entity with provided id in repository
     * @throws ValidationException if entity with provided id is not present in repository
     */
    T find(Long id);

    /**
     * Retrieves entities from repository according to provided query.
     *
     * @param searchParams params for search query
     * @param orderParams params for order query
     * @param paginationDTO DTO for pagination
     * @return List<T> list of entities from repository according to provided query
     * @throws ValidationException if provided query is not valid or entities according to provided query
     *                          are not present in repository
     */
    Page<T> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams, Pageable pageable);

}

