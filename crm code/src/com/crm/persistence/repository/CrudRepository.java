package com.crm.persistence.repository;

<<<<<<< Updated upstream
public interface CrudRepository<T> {
    void save(T entity);
    T findById(String id);
    void update(T entity);
=======

public interface CrudRepository<T> {

    void save(String id,T entity);
    T findById(String id);


    void update(String id, T entity);

>>>>>>> Stashed changes
    void delete(String id);
}
