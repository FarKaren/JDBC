package dao;

public interface Repository<T> {
     T find(long id);
     void save(T t);
     void update(T t);
     void delete(long id);
}
