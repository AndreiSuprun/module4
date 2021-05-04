package com.epam.esm.dao;

import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;

import java.util.List;
import java.util.Optional;
/**
 * DAO interface responsible for processing CRUD operations for entities
 *
 * @author Andrei Suprun
 */
public interface GenericDao<T> {

     /**
      * Returns Optional of object for entity with provided id from repository.
      *
      * @param id id of entity to find
      * @return Optional<T> of entity with provided id in repository
      */
     T findOne(Long id);

     /**
      * Returns list of objects of all entities from repository.
      *
      * @return List of entities in repository
      */
     List<T> findAll(Integer page, Integer size) ;

     /**
      * Retrieves entities from repository according to provided query.
      *
      * @param searchParams search params for building search query
      * @param sortParams sort params for building search query
      * @param page number of page
      * @param size size of page
      * @return List<T> list of entities from repository according to provided query
      */
     List<T> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Integer page, Integer size);

     /**
      * Retrieves entity from repository according to provided name.
      *
      * @param name name of entity to find in repository
      * @return Optional<T> optional of entity from repository according to provided name
      */
     T findByName(String name);

     /**
      * Counts entity in repository according to provided search params or if params are not provided counts
      * all entities in repository.
      *
      * @param searchParams search params to count entities in repository
      * @return Long number of entities in repository according to provided search params
      */
     Long count(List<SearchCriteria>... searchParams);

     /**
      * Adds entity object to repository.
      *
      * @param obj of entity to add to repository
      * @return object of entity in repository
      */
     T insert(T obj);

     /**
      * Updates object for entity with provided id in repository.
      *
      * @param obj of entity to update
      * @param id of entity to update
      * @return object of updated entity in repository
      */
     T update(T obj, Long id);

     /**
      * Removes entity with provided id from repository.
      *
      * @param id id of entity to remove
      * @return true if entity with provided id was removed from repository
      */
     boolean delete(Long id);
}
