package ru.tycce.springboot.database.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class TestRun implements Comparable<TestRun>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime startTestTime;
    private LocalDateTime endTestTime;
    private boolean deprecate;
    private boolean isRun;

    @Override
    public int compareTo(TestRun o) {
        return startTestTime.compareTo(o.startTestTime);
    }

    public String convertDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("HH.mm"));
    }

    public TestRun getThis(){
        return this;
    }
}
