package com.crm.persistence.repository;


public interface CrudRepository<T> {

    void save(String id,T entity);
    T findById(String id);


    void update(String id, T entity);

    void delete(String id);
}
