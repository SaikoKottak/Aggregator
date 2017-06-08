package com.javarush.task.task28.task2810;

import com.javarush.task.task28.task2810.model.Provider;
import com.javarush.task.task28.task2810.model.Strategy;

public class Aggregator {
    public static void main(String[] args) {
        Provider mockProvider = new Provider(new Strategy() {
        });
        Controller mockController = new Controller(mockProvider);
        System.out.println(mockController);
    }
}
