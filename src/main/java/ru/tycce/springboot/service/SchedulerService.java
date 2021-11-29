package ru.tycce.springboot.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tycce.springboot.database.DatabaseService;
import ru.tycce.springboot.database.entity.TestRun;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


@Service
@Slf4j
@Data
public class SchedulerService{

    private final Map<TestRun, ScheduledFuture<?>> testRunScheduledFutureMap;
    private final DatabaseService databaseService;
    private final ScheduledExecutorService scheduledExecutorService;

    public SchedulerService(DatabaseService databaseService){
        this.databaseService = databaseService;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.testRunScheduledFutureMap = new ConcurrentHashMap<>();

        scheduleTests();
        clearDeprecatedTestRun();
    }

    private void scheduleTests() {
        databaseService.getAllTestRuns().stream().filter(e->!e.isDeprecate()).forEach(this::scheduleTest);
    }

    public TestRun getNextScheduledTestRun(){
        return testRunScheduledFutureMap.keySet().stream().min(TestRun::compareTo).orElse(null);
    }

    private void clearDeprecatedTestRun() {
        LocalDateTime now = LocalDateTime.now();
        testRunScheduledFutureMap.keySet().stream().filter(tr -> tr.getStartTestTime().isBefore(now)).forEach(this::deleteScheduleTest);
    }

    public TestRun addScheduleTest(TestRun testRun){
        if(checkScheduledDateTime(testRun)) {
            testRun = databaseService.addOrUpdateTestRun(testRun);
            scheduleTest(testRun);
            log.info("TestRun запланирован: {}, future: {}", testRun, testRunScheduledFutureMap.get(testRun));
            return testRun;
        }
        else{
            return null;
        }
    }

    private boolean checkScheduledDateTime(TestRun testRun) {
        if(testRun.getStartTestTime().isBefore(LocalDateTime.now())) return false;

        for (TestRun scheduledTestRun: testRunScheduledFutureMap.keySet()) {
            if(!((testRun.getStartTestTime().isBefore(scheduledTestRun.getStartTestTime()) && testRun.getEndTestTime().isBefore(scheduledTestRun.getStartTestTime()))
                    || (testRun.getStartTestTime().isAfter(scheduledTestRun.getEndTestTime()) && testRun.getEndTestTime().isAfter(scheduledTestRun.getEndTestTime())))){
                return false;
            }
        }
        return true;
    }

    private void scheduleTest(TestRun testRun){
        testRunScheduledFutureMap.put(testRun, scheduledExecutorService.schedule(() -> startTest(testRun), getMilliBeforeDateStart(testRun.getStartTestTime()), TimeUnit.MILLISECONDS));
    }

    private void startTest(TestRun testRun){
        log.info("TestRun запущен: {}, future: {}", testRun, testRunScheduledFutureMap.get(testRun));
        testRun.setRun(true);
        databaseService.addOrUpdateTestRun(testRun);

        try {
            Thread.sleep(getMilliBeforeDateStart(testRun.getEndTestTime()));
            log.info("TestRun завершен: {}, future: {}", testRun, testRunScheduledFutureMap.get(testRun));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        testRun.setRun(false);
        databaseService.addOrUpdateTestRun(testRun);
        deleteScheduleTest(testRun);
    }

    private long getMilliBeforeDateStart(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void deleteScheduleTest(TestRun testRun) {
        testRunScheduledFutureMap.get(testRun).cancel(true);
        testRunScheduledFutureMap.remove(testRun);

        testRun.setRun(true);
        testRun.setDeprecate(true);
        databaseService.addOrUpdateTestRun(testRun);
        log.info("TestRun was marked as deprecated: {}, future: {}", testRun, testRunScheduledFutureMap.get(testRun));
    }

}

