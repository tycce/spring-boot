package ru.tycce.springboot.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tycce.springboot.database.entity.Card;
import ru.tycce.springboot.database.entity.Task;
import ru.tycce.springboot.database.repository.CardRepository;
import ru.tycce.springboot.database.repository.TaskRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseService extends AbstractDatabaseService{
    private final CardRepository cardRepository;
    private final TaskRepository taskRepository;

    public Card getCardById(int id) {
        return getEntityById(id, cardRepository);
    }
    public Card addOrUpdateCard(Card card) {
        return saveOrUpdate(card, cardRepository);
    }
    public boolean deleteCardById(int id){
        return deleteById(id, cardRepository);
    }
    public List<Card> getAllCards(){
        return getEntitiesToList(cardRepository.findAll());
    }

    public Task getTaskById(int id) {
        return getEntityById(id, taskRepository);
    }
    public Task addOrUpdateTask(Task task) {
        return saveOrUpdate(task, taskRepository);
    }
    public boolean deleteTaskById(int id){
        return deleteById(id, taskRepository);
    }
    public List<Task> getAllTasks(){
        return getEntitiesToList(taskRepository.findAll());
    }
    public List<Task> addOrUpdateAllTask(List<Task> tasks){return saveAll(tasks, taskRepository);}


}
