package workWithAtomics;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger counter3 = new AtomicInteger();
    public static AtomicInteger counter4 = new AtomicInteger();
    public static AtomicInteger counter5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        List<Thread> threads = new ArrayList<>();

        //Thread 1 - Count palindrome
        Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                int left = 0;
                int right = text.length() - 1;
                while (left < right) {
                    if (text.charAt(left) == text.charAt(right)) {
                        left++;
                        right--;
                        if (left >= right) {

                            //вместо нескольких while-loops переписала что 1 loop и потом проверяется длинна "красивого" слова
                            if (text.length() == 3) {
                                counter3.addAndGet(1);
                            } else if (text.length() == 4) {
                                counter4.addAndGet(1);
                            } else {
                                counter5.addAndGet(1);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        });
        thread1.start();
        threads.add(thread1);

        //Thread 2 - Count words with the same letters
        Thread thread2 = new Thread(() -> {
            for (String text : texts) {

                Set<Character> letters = new HashSet<>();
                for (char c : text.toCharArray()) {
                    letters.add(c);
                }
                if (letters.size() == 1) {

                    //вместо нескольких while-loops переписала что 1 loop и потом проверяется длинна "красивого" слова
                    if (text.length() == 3) {
                        counter3.addAndGet(1);
                    } else if (text.length() == 4) {
                        counter4.addAndGet(1);
                    } else {
                        counter5.addAndGet(1);
                    }
                }
            }
        });
        thread2.start();
        threads.add(thread2);

        //Thread 3 - Count words with ascending letters
        Thread thread3 = new Thread(() -> {
            for (String text : texts) {
                int i = 1;
                while (i < text.length()) {
                    if (text.charAt(i - 1) <= text.charAt(i)) {
                        i++;
                        if (i == text.length()) {

                            //вместо нескольких while-loops переписала что 1 loop и потом проверяется длинна "красивого" слова
                            if (text.length() == 3) {
                                counter3.addAndGet(1);
                            } else if (text.length() == 4) {
                                counter4.addAndGet(1);
                            } else {
                                counter5.addAndGet(1);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        });
        thread3.start();
        threads.add(thread3);

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.printf("Красивых слов с длиной 3: %d шт\n" +
                "Красивых слов с длиной 4: %d шт\n" +
                "Красивых слов с длиной 5: %d шт", counter3.get(), counter4.get(), counter5.get());

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
