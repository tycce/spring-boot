package ru.tycce.springboot.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AbstractDatabaseService {

    protected <T> T saveOrUpdate(T entity, CrudRepository<T, ?> repository) {
        repository.save(entity);
        return entity;
    }

    protected  <T, ID> T getEntityById(ID id, CrudRepository<T, ID> repository) {
        return repository.findById(id).orElse(null);
    }

    protected <ID> boolean deleteById(ID id, CrudRepository<?, ID> repository) {
        try{
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("fail to delete by id: {} repository: {}", id, repository);
            throw e;
        }
    }

    protected <T> List<T> saveAll(List<T> list, CrudRepository<T, ?> repository) {
        return getEntitiesToList(repository.saveAll(list));
    }

    protected <T> List<T> getEntitiesToList(Iterable<T> entities){
        List<T> list = new ArrayList<>();
        entities.forEach(list::add);
        return list;
    }
}
