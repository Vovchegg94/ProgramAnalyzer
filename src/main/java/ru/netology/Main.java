package ru.netology;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void logic(String[] texts, BlockingQueue<String> queueFor, char a) {
        int maxValue = 0;
        for (int i = 0; i < texts.length; i++) {
            int count = 0;
            try {
                String text = queueFor.take();
                for (int j = 0; j < text.length(); j++) {

                    if (text.charAt(j) == a) {
                        count++;
                    }
                }

                if (count > maxValue) {
                    maxValue = count;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println("Максимальное значение символов (" + a + "): " + maxValue);

    }

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
            logic(texts, queueForA, 'a');
        }));


        threads.add(new Thread(() -> {
            logic(texts, queueForB, 'b');
        }));


        threads.add(new Thread(() -> {
            logic(texts, queueForC, 'c');
        }));


        for (Thread thread : threads) {
            thread.start();

        }
        for (Thread thread : threads) {

            thread.join();
        }

    }
}