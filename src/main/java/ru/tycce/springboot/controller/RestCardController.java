package ru.tycce.springboot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tycce.springboot.database.DatabaseService;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
@Slf4j
public class RestCardController {

    private final DatabaseService databaseService;


}
