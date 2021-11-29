package ru.tycce.springboot.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tycce.springboot.database.DatabaseService;
import ru.tycce.springboot.database.entity.Card;

@RestController
@RequiredArgsConstructor
public class RestCardController {

//    private final DatabaseService databaseService;
//
//    @GetMapping()
//    public ResponseEntity<Card> getAllCard(){
//        return new ResponseEntity<Card>(databaseService.getAllCards(), HttpStatus.OK);
//    }
}
