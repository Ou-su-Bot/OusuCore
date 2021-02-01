package me.skiincraft.sql.repository;

import me.skiincraft.sql.platform.UseStatement;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> extends UseStatement {

    Optional<T> get(int index);
    Optional<T> getById(ID id);
    Optional<T> getByRow(String row, Object value);
    List<T> getAll();
    boolean contains(T item);

    long size();
    void remove(int index);
    void removeObject(T index);
    void save(T item);

}
