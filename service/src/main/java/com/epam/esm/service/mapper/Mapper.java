package com.epam.esm.service.mapper;

public interface Mapper<T, S> {

    S mapEntityToDTO(T entity);
    T mapDtoToEntity(S dto);
}
