package ru.tycce.springboot.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tycce.springboot.database.entity.Card;
import ru.tycce.springboot.database.entity.TestRun;
import ru.tycce.springboot.database.repository.CardRepository;
import ru.tycce.springboot.database.repository.TestRunRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseService extends AbstractDatabaseService{
    private final CardRepository cardRepository;
    private final TestRunRepository testRunRepository;

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

    public TestRun getTestRunById(int id) {
        return getEntityById(id, testRunRepository);
    }
    public TestRun addOrUpdateTestRun(TestRun testRun) {
        return saveOrUpdate(testRun, testRunRepository);
    }
    public boolean deleteTestRunById(int id){
        return deleteById(id, testRunRepository);
    }
    public List<TestRun> getAllTestRuns(){
        return getEntitiesToList(testRunRepository.findAll());
    }


}
