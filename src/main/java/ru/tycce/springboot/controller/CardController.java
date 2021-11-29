package ru.tycce.springboot.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tycce.springboot.database.DatabaseService;
import ru.tycce.springboot.database.entity.Card;
import ru.tycce.springboot.service.SystemService;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@Slf4j
@Data
@RequestMapping("card/")
public class CardController {

    private final DatabaseService databaseService;
    private final SystemService systemService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("cards", databaseService.getAllCards());
        log.info("hipSize: {}", systemService.hipSize() / 1024 / 1024);
        log.info("freeHipSize: {}", systemService.freeHipSize() / 1024 / 1024);
        log.info("maxHipSize: {}", systemService.maxHipSize() / 1024 / 1024);
        log.info("getSystemCPULoadPercent: {}", systemService.getSystemCPULoadPercent());
        return "index";
    }

    @PostMapping
    public String add(@RequestParam(value = "name") String name, @RequestParam(value = "file") MultipartFile file){
        if(file != null) {
            String[] extension = file.getOriginalFilename().split("\\.");
            String resultFilename = UUID.randomUUID().toString() + "." + extension[extension.length - 1];
            databaseService.addOrUpdateCard(Card.builder().name(name).imageUrl(resultFilename).build());
            try{
                file.transferTo(new File(resultFilename));
            }catch (IOException e){
                log.error("Exception: {}, не удалось загрузить файл: {}", e, resultFilename);
            }

        }

        return "redirect:/";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("card") Card card) {
        databaseService.addOrUpdateCard(card);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        databaseService.deleteCardById(id);
        return "redirect:/";
    }
}
