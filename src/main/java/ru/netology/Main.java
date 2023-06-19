package ru.netology;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static int maxA = 0;
    static int maxB = 0;
    static int maxC = 0;

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queueForA = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queueForB = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> queueForC = new ArrayBlockingQueue<>(100);

        List<Thread> threads = new ArrayList<>();
        String[] texts = new String[10_000];


        new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {

                texts[i] = generateText("abc", 100_000);

                try {
                    queueForA.put(texts[i]);
                    queueForB.put(texts[i]);
                    queueForC.put(texts[i]);
                } catch (InterruptedException e) {
                    return;
                }

            }
        }).start();

        threads.add(new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                int countA = 0;
                try {
                    String text = queueForA.take();
                    for (int a = 0; a < text.length(); a++) {

                        if (text.charAt(a) == 'a') {
                            countA++;
                        }
                    }

                    if (countA > maxA) {
                        maxA = countA;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }));


        threads.add(new Thread(() -> {
            for (int i = 0; i < texts.length; i++) {
                int countB = 0;
                try {
                    String text = queueForB.take();
                    for (int a = 0; a < text.length(); a++) {

                        if (text.charAt(a) == 'b') {
                            countB++;
                        }
                    }
                    if (countB > maxB) {
                        maxB = countB;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }));


        threads.add(new Thread(() -> {

                    for (int i = 0; i < texts.length; i++) {
                        int countC = 0;
                        try {
                            String text = queueForC.take();
                            for (int a = 0; a < text.length(); a++) {

                                if (text.charAt(a) == 'c') {
                                    countC++;
                                }
                            }
                            if (countC > maxC) {
                                maxC = countC;
                            }
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                })
        );


        for (Thread thread : threads) {
            thread.start();

        }
        for (Thread thread : threads) {

            thread.join();
        }

        System.out.println("Максимальное значение символов (a): " + maxA);
        System.out.println("Максимальное значение символов (b): " + maxB);
        System.out.println("Максимальное значение символов (c): " + maxC);
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}