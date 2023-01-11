package dao;

public interface DAO<T> {
     T find(long id);
     void save(T t);
     void update(T t);
     void delete(long id);
}
