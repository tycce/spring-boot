package ru.tycce.springboot.database.repository;

import org.springframework.data.repository.CrudRepository;
import ru.tycce.springboot.database.entity.Card;

public interface CardRepository extends CrudRepository<Card, Integer> {
}
