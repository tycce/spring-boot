package ru.tycce.springboot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tycce.springboot.database.DatabaseService;

@SpringBootTest
@RequiredArgsConstructor
class SpringbootApplicationTests {

    private final DatabaseService databaseService;

    @Test
    void contextLoads() {


    }

}
