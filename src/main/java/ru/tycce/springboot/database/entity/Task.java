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
public class Task implements Comparable<Task>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean deprecate;
    private boolean isRun;

    @Override
    public int compareTo(Task o) {
        return startTime.compareTo(o.startTime);
    }

    public String convertDate(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("HH.mm"));
    }

    public Task getThis(){
        return this;
    }
}
