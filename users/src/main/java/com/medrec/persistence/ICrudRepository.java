package com.medrec.persistence;

import java.util.List;

public interface ICrudRepository<T> {
    ResponseMessage save(T entity);
    T findById(int id);
    List<T> findAll();
    ResponseMessage update(T entity);
    ResponseMessage delete(int id);
}
