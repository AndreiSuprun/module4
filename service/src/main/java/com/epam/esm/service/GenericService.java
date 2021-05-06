package com.epam.esm.service;

import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;

import java.util.List;

public interface GenericService<T> {

    final String PAGE_PARAMETER = "page";

    /**
     * Returns DTO object for entity with provided id from repository.
     *
     * @param id id of entity to find
     * @return DTO object of entity with provided id in repository
     * @throws ProjectException if entity with provided id is not present in repository
     */
    T find(Long id);

    /**
     * Returns all DTO objects of entities from repository.
     *
     * @return list of DTO objects of retrieved entities
     */
    List<T> findAll(PaginationDTO paginationDTO);

    /**
     * Retrieves entities from repository according to provided query.
     *
     * @param searchParams params for search query
     * @param orderParams params for order query
     * @param paginationDTO DTO for pagination
     * @return List<T> list of entities from repository according to provided query
     * @throws ProjectException if provided query is not valid or entities according to provided query
     *                          are not present in repository
     */
    List<T> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams, PaginationDTO paginationDTO);

    default void checkPagination(PaginationDTO paginationDTO) {
        if (paginationDTO.getPage() == null || paginationDTO.getPage() <= 0) {
            paginationDTO.setPage(PaginationDTO.FIRST_PAGE);
        }
        if (paginationDTO.getSize() == null || paginationDTO.getSize() <= 0) {
            paginationDTO.setSize(PaginationDTO.DEFAULT_RECORDS_PER_PAGE);
        }
    }

    default void checkPageNumber(PaginationDTO paginationDTO, Long count) {
        if (((long) (paginationDTO.getPage() - 1) * paginationDTO.getSize()) > count) {
            throw new ProjectException(ErrorCode.QUERY_PARAMETER_INVALID, PAGE_PARAMETER, paginationDTO.getPage());
        }
        paginationDTO.setTotalCount(count);
        long totalPages = count % paginationDTO.getSize() > 0 ? count / paginationDTO.getSize() + 1 :
                count / paginationDTO.getSize();
        paginationDTO.setTotalPages(totalPages);
    }
}

