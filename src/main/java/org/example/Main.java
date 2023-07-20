package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

public class Main {
    static ArrayBlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    static final int AMOUNT_OF_TEXT = 10000;
    static final int LENGTH_TEXT = 100000;
    static final String TEMPLATE = "abc";

    public static void main(String[] args) throws InterruptedException {


        Thread fillQueue = new Thread(() -> {
            for (int i = 0; i < AMOUNT_OF_TEXT; i++) {
                String temp = generateText(TEMPLATE, LENGTH_TEXT);
                try {
                    queueA.put(temp);
                    queueB.add(temp);
                    queueC.add(temp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        fillQueue.start();

        Thread findA = new Thread(() -> {
            char c = 'a';
            int maxCountA = findMaxCountChars(queueA, c);
            System.out.println("Максимальное количество символов \"a\" в строчке: " + maxCountA);
        });
        findA.start();

        Thread findB = new Thread(() -> {
            char c = 'b';
            int maxCountB = findMaxCountChars(queueB, c);
            System.out.println("Максимальное количество символов \"b\" в строчке: " + maxCountB);
        });
        findB.start();

        Thread findC = new Thread(() -> {
            char c = 'c';
            int maxCountC = findMaxCountChars(queueC, c);
            System.out.println("Максимальное количество символов \"b\" в строчке: " + maxCountC);
        });
        findC.start();

        findA.join();
        findB.join();
        findC.join();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int findMaxCountChars(ArrayBlockingQueue<String> queue, char c) {
        int max = 0;
        int tempCount;
        String text;
        try {
            for (int i = 0; i < AMOUNT_OF_TEXT; i++) {
                text = queue.take();
                tempCount = (int) text.chars()
                        .filter(x -> x == c)
                        .count();
                if (tempCount > max) max = tempCount;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "was interrupted");
            return -1;
        }
        return max;
    }
}