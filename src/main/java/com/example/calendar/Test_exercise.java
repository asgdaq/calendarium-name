package com.example.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Test_exercise {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(":) - > " + localDateTime);

        DateTimeFormatter newlocaldatetime = DateTimeFormatter.ofPattern("E dd/MM/yyyy");
        String localtimenew = localDateTime.format(newlocaldatetime);
        System.out.println(localtimenew);

    }
}
