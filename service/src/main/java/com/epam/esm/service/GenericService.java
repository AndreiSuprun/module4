package com.epam.esm.service;

import com.epam.esm.service.dto.PaginationDTO;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;

import java.util.List;

/**
 * Service class responsible for processing generic operations
 *
 * @author Andrei Suprun
 */
public interface GenericService<T> {

    final String PAGE_PARAMETER = "page";

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
    List<T> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> orderParams, PaginationDTO paginationDTO);

    /**
     * Check pagination parameters according to provided query. Set default values for page number and page size if
     * pagination parameters are not present in request
     *
     * @param paginationDTO params for pagination
     * @throws ValidationException if provided pagination parameters is not valid
     */
    default void checkPagination(PaginationDTO paginationDTO) {
        if (paginationDTO.getPage() == null) {
            paginationDTO.setPage(PaginationDTO.FIRST_PAGE);
        }
        if (paginationDTO.getPage() <= 0) {
            throw new ValidationException(ErrorCode.QUERY_PARAMETER_INVALID, PAGE_PARAMETER, paginationDTO.getPage());
        }
        if (paginationDTO.getSize() == null) {
            paginationDTO.setSize(PaginationDTO.DEFAULT_RECORDS_PER_PAGE);
        }
        if (paginationDTO.getSize() <= 0) {
            throw new ValidationException(ErrorCode.QUERY_PARAMETER_INVALID, PAGE_PARAMETER, paginationDTO.getSize());
        }
    }

    /**
     * Check page number in provided query. Set default values for page number if it is out of range
     *
     * @param paginationDTO params for pagination
     * @param count count of objects in repository according to request
     */
    default void checkPageNumber(PaginationDTO paginationDTO, Long count) {
        if (((long) (paginationDTO.getPage() - 1) * paginationDTO.getSize()) > count) {
            paginationDTO.setPage(PaginationDTO.FIRST_PAGE);
        }
        paginationDTO.setTotalCount(count);
        long totalPages = count % paginationDTO.getSize() > 0 ? count / paginationDTO.getSize() + 1 :
                count / paginationDTO.getSize();
        paginationDTO.setTotalPages(totalPages);
    }
}

