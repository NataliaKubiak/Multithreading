package collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queueA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueC = new ArrayBlockingQueue<>(100);

    private static final String LETTERS = "abc";
    private static final int STRING_LENGTH = 100_000;
    private static final int STRING_AMOUNT = 10_000;

    //данные для вывода String с наиб. кол-вом 'а' и числа сколько раз 'а' повторяется в этой строке
    public static String stringA = "";
    public static int aMaxCount = -1;
    //данные для вывода String с наиб. кол-вом 'b' и числа сколько раз 'b' повторяется в этой строке
    public static String stringB = "";
    public static int bMaxCount = -1;
    //данные для вывода String с наиб. кол-вом 'c' и числа сколько раз 'c' повторяется в этой строке
    public static String stringC = "";
    public static int cMaxCount = -1;

    public static void main(String[] args) throws InterruptedException {

        long startTs = System.currentTimeMillis(); // start time
        Thread generateQueues = new Thread(() -> {
            for (int i = 0; i < STRING_AMOUNT; i++) {
                try {
                    queueA.put(generateText(LETTERS, STRING_LENGTH));
                    queueB.put(generateText(LETTERS, STRING_LENGTH));
                    queueC.put(generateText(LETTERS, STRING_LENGTH));
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        generateQueues.start();

        List<Thread> threads = new ArrayList<>();

        Thread countA = new Thread(() -> {
            int letterCounter = 0;

            for (int j = 0; j < STRING_AMOUNT; j++) {
                try {
                    String str = queueA.take();
                    for (int i = 0; i < str.length(); i++) {
                        if (str.charAt(i) == 'a') {
                            letterCounter++;
                            if (letterCounter > aMaxCount) {
                                aMaxCount = letterCounter;
                                stringA = str;
                            }
                        } else {
                            letterCounter = 0;
                        }
                    }
                } catch (InterruptedException ex) {
                    return;
                }
            }
        });
        countA.start();
        threads.add(countA);

        Thread countB = new Thread(() -> {
            int letterCounter = 0;

            for (int j = 0; j < STRING_AMOUNT; j++) {
                try {
                    String str = queueB.take();
                    for (int i = 0; i < str.length(); i++) {
                        if (str.charAt(i) == 'b') {
                            letterCounter++;
                            if (letterCounter > bMaxCount) {
                                bMaxCount = letterCounter;
                                stringB = str;
                            }
                        } else {
                            letterCounter = 0;
                        }
                    }
                } catch (InterruptedException ex) {
                    return;
                }
            }
        });
        countB.start();
        threads.add(countB);

        Thread countC = new Thread(() -> {
            int letterCounter = 0;

            for (int j = 0; j < STRING_AMOUNT; j++) {
                try {
                    String str = queueC.take();
                    for (int i = 0; i < str.length(); i++) {
                        if (str.charAt(i) == 'c') {
                            letterCounter++;
                            if (letterCounter > cMaxCount) {
                                cMaxCount = letterCounter;
                                stringC = str;
                            }
                        } else {
                            letterCounter = 0;
                        }
                    }
                } catch (InterruptedException ex) {
                    return;
                }
            }
        });
        countC.start();
        threads.add(countC);

        for (Thread thread : threads) {
            thread.join();
        }

        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");

        System.out.println("Наибольшее количество 'a' = " + aMaxCount + " встречается в строке: " + stringA.substring(0, 100));
        System.out.println("Наибольшее количество 'b' = " + bMaxCount + " встречается в строке: " + stringB.substring(0, 100));
        System.out.println("Наибольшее количество 'c' = " + cMaxCount + " встречается в строке: " + stringC.substring(0, 100));
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
