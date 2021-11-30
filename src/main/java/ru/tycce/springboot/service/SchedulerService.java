package ru.tycce.springboot.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tycce.springboot.database.DatabaseService;
import ru.tycce.springboot.database.entity.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@Data
public class SchedulerService{

    private final Map<Task, ScheduledFuture<?>> taskScheduledFutureMap;
    private final DatabaseService databaseService;
    private final ScheduledExecutorService scheduledExecutorService;

    public SchedulerService(DatabaseService databaseService){
        this.databaseService = databaseService;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.taskScheduledFutureMap = new ConcurrentHashMap<>();

        clearDeprecatedTask();
        scheduleTests();
    }

    private void scheduleTests() {
        databaseService.getAllTasks().stream().filter(e->!e.isDeprecate()).forEach(this::scheduleTest);
    }

    public Task getNextScheduledTask(){
        return taskScheduledFutureMap.keySet().stream().min(Task::compareTo).orElse(null);
    }

    private void clearDeprecatedTask() {
        LocalDateTime now = LocalDateTime.now();
        databaseService.addOrUpdateAllTask(databaseService.getAllTasks()
                .stream()
                .filter(e->e.getStartTime().isBefore(now))
                .peek(e->e.setDeprecate(true))
                .peek(e->e.setRun(false))
                .collect(Collectors.toList()));
    }

    public Task addScheduleTest(Task task){
        if(checkScheduledDateTime(task)) {
            task = databaseService.addOrUpdateTask(task);
            scheduleTest(task);
            log.info("Task запланирован: {}, future: {}", task, taskScheduledFutureMap.get(task));
            return task;
        }
        else{
            return null;
        }
    }

    private boolean checkScheduledDateTime(Task task) {
        if(task.getStartTime().isBefore(LocalDateTime.now())) return false;

        for (Task scheduledTask: taskScheduledFutureMap.keySet()) {
            if(!((task.getStartTime().isBefore(scheduledTask.getStartTime()) && task.getEndTime().isBefore(scheduledTask.getStartTime()))
                    || (task.getStartTime().isAfter(scheduledTask.getEndTime()) && task.getEndTime().isAfter(scheduledTask.getEndTime())))){
                return false;
            }
        }
        return true;
    }

    private void scheduleTest(Task task){
        taskScheduledFutureMap.put(task, scheduledExecutorService.schedule(() -> startTest(task), getMilliBeforeDateStart(task.getStartTime()), TimeUnit.MILLISECONDS));
    }

    private void startTest(Task task){
        log.info("Task запущен: {}, future: {}", task, taskScheduledFutureMap.get(task));
        task.setRun(true);
        databaseService.addOrUpdateTask(task);

        try {
            Thread.sleep(getMilliBeforeDateStart(task.getEndTime()));
            log.info("Task завершен: {}, future: {}", task, taskScheduledFutureMap.get(task));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        databaseService.addOrUpdateTask(task);
        deleteScheduleTest(task);
    }

    private long getMilliBeforeDateStart(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                - LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public void deleteScheduleTest(Task task) {
        ScheduledFuture<?> future = taskScheduledFutureMap.get(task);
        if(future != null) taskScheduledFutureMap.get(task).cancel(true);
        taskScheduledFutureMap.remove(task);

        task.setRun(false);
        task.setDeprecate(true);
        databaseService.addOrUpdateTask(task);
        log.info("Task was marked as deprecated: {}, future: {}", task, taskScheduledFutureMap.get(task));
    }

}

