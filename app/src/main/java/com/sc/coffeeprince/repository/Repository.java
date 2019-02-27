package com.sc.coffeeprince.repository;

import java.util.List;

public interface Repository<T, E> {

    void add(T t);

    T find(E id);

    List<T> finds(E id);

    List<T> findAll();

    void update(T t);

    void delete(E id);
}
