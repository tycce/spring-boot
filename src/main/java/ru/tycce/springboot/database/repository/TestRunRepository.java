package ru.tycce.springboot.database.repository;

import org.springframework.data.repository.CrudRepository;
import ru.tycce.springboot.database.entity.TestRun;

public interface TestRunRepository extends CrudRepository<TestRun, Integer> {
}
