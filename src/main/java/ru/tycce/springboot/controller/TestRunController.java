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
import ru.tycce.springboot.database.entity.TestRun;
import ru.tycce.springboot.service.SchedulerService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("")
public class TestRunController {

    private final DatabaseService databaseService;
    private final SchedulerService scheduler;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("testRun", new TestRun());
        model.addAttribute("testRuns", databaseService.getAllTestRuns());

//        model.addAttribute("testRunScheduledFutureMap", scheduler.getTestRunScheduledFutureMap().values().toString());
        model.addAttribute("nextScheduledTestRun", scheduler.getNextScheduledTestRun());
        return "testRun";
    }

    @PostMapping("/")
    public String addTestRun(
            @RequestParam("dateStart") String dateStart,
            @RequestParam("deprecate") boolean deprecate,
            @RequestParam("run") boolean run
    ) {
        TestRun testRun = new TestRun();

        LocalDateTime localDateTime = LocalDateTime.parse(dateStart, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        testRun.setStartTestTime(localDateTime);
        testRun.setEndTestTime(localDateTime.plusMinutes(1));
        testRun.setDeprecate(deprecate);
        testRun.setRun(run);


        if(scheduler.addScheduleTest(testRun) == null) {
            log.error("Не удалось добавить задачу");
        }

        return "redirect:/";
    }
}
