package ru.tycce.springboot.database.repository;

import org.springframework.data.repository.CrudRepository;
import ru.tycce.springboot.database.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Integer> {
}
