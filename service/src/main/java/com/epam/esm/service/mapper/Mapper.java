package com.epam.esm.service.mapper;

/**
 * Service class for mapping entities to/from DTO
 *
 * @author Andrei Suprun
 */
public interface Mapper<T, S> {

    /**
     * Maps entity object to DTO object.
     *
     * @param entity entity object for mapping
     * @return DTO object
     */
    S mapEntityToDTO(T entity);

    /**
     * Maps DTO object to entity object.
     *
     * @param dto object for mapping
     * @return entity object
     */
    T mapDtoToEntity(S dto);
}
