package app.mvc.model;

import java.util.List;

public interface DAO<T> {
    List<T> findAll();
    void insert(T t);
    void update(T t);
    void delete(int id);
}