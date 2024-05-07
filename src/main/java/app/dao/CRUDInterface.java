package app.dao;

import java.util.List;
import java.util.Optional;

public interface CRUDInterface<T> {
    long add(T t);

    Optional<T> getById(long id);

    List<T> getAll();

    boolean update(T t);

    boolean delete(T t);
}
