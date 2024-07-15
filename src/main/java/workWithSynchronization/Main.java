package workWithSynchronization;

import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        //в каждом потоке считаем общее количество R в строке
        Runnable logic = () -> {
            String route = generateRoute("RLRFR", 100);
            int counter = 0;

            for (int i = 0; i < route.length(); i++) {
                if (route.charAt(i) == 'R') {
                    counter++;
                }
            }
            synchronized (sizeToFreq) {
                sizeToFreq.put(counter, sizeToFreq.getOrDefault(counter, 0) + 1);
            }
        };

        int threadsCount = 1000;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new Thread(logic);
            thread.start();
            threads.add(thread);
        }

        //ждем пока все threads завершат работу
        for (Thread thread : threads) {
            thread.join();
        }

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
