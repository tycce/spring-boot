package ru.tycce.springboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tycce.springboot.database.DatabaseService;
import ru.tycce.springboot.database.entity.Task;
import ru.tycce.springboot.service.SchedulerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("")
public class TaskController {

    private final DatabaseService databaseService;
    private final SchedulerService scheduler;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("tasks", databaseService.getAllTasks());

//        model.addAttribute("taskScheduledFutureMap", scheduler.getTaskScheduledFutureMap().values().toString());
        model.addAttribute("nextScheduledTask", scheduler.getNextScheduledTask());
        return "task";
    }

    @PostMapping("/")
    public String addTask(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("deprecate") boolean deprecate,
            @RequestParam("run") boolean run
    ) {
        Task task = new Task();

        LocalDateTime localDateTime = LocalDateTime.parse(dateStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        task.setStartTime(localDateTime);
        task.setEndTime(localDateTime.plusMinutes(1));
        task.setDeprecate(deprecate);
        task.setRun(run);


        if(scheduler.addScheduleTest(task) == null) {
            log.error("Не удалось добавить задачу");
        }

        return "redirect:/";
    }
}
