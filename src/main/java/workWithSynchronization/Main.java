package workWithSynchronization;

import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static int THREADS_COUNT = 1000;

    public static void main(String[] args) throws InterruptedException {

        //в каждом потоке считаем общее количество R в строке
        Runnable countR = () -> {
            String route = generateRoute("RLRFR", 100);
            int counter = 0;

            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == 'R') {
                    counter++;
                }
            }
            synchronized (sizeToFreq) {
                sizeToFreq.put(counter, sizeToFreq.getOrDefault(counter, 0) + 1);
                sizeToFreq.notify();
            }
        };

        Runnable showMax = () -> {
            while (!Thread.interrupted()) {
                int maxKey = -1;
                int maxValue = Integer.MIN_VALUE;

                synchronized (sizeToFreq) {
                        try {
                            sizeToFreq.wait();
                        } catch (InterruptedException e) {
                            return;
                        }

                    for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
                        int currentValue = entry.getValue();
                        if (currentValue > maxValue) {
                            maxValue = currentValue;
                            maxKey = entry.getKey();
                        }
                    }
                    System.out.println("Текущий лидер среди частот: " + maxKey);
                }
            }
        };

        Thread threadMax = new Thread(showMax);
        threadMax.start();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < THREADS_COUNT; i++) {
            Thread threadCountR = new Thread(countR);
            threadCountR.start();
            threads.add(threadCountR);
        }

        //ждем пока все threads завершат работу
        for (Thread thread : threads) {
            thread.join();
        }
        threadMax.interrupt();

        //вычисляем наибольшее количество повторений
        int maxKey = -1;
        int maxValue = Integer.MIN_VALUE;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int currentValue = entry.getValue();
            if (currentValue > maxValue) {
                maxValue = currentValue;
                maxKey = entry.getKey();
            }
        }

        //выводим в консоль результаты
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxKey, maxValue);
        sizeToFreq.remove(maxKey);
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            System.out.println("- " + entry.getKey() + "(" + entry.getValue() + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
