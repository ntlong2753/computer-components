package com.codegym.computercomponents.service;

import java.util.List;

public interface GeneralService<T, ID> {

    List<T> findAll();

    T findById(ID id);

    T save(T entity);

    void deleteById(ID id);
}