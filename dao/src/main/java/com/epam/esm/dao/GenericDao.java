package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.io.Serializable;
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
     Optional<T> findOne(Long id);

     /**
      * Returns list of objects of all entities from repository.
      *
      * @return List of entities in repository
      */
     List<T> findAll() ;

     /**
      * Retrieves entity from repository according to provided name.
      *
      * @param name name of entity to find in repository
      * @return Optional<T> optional of entity from repository according to provided name
      */
     Optional<T> findByName(String name);

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
