package com.epam.esm.service.mapper;

import org.springframework.stereotype.Service;


public interface Mapper<T, V> {

    V mapEntityToDTO(T entity);
    T mapDtoToEntity(V dto);
}
