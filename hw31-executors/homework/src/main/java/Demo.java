import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo {
    /*
     * Два потока печатают числа от 1 до 10, потом от 10 до 1.
     * Надо сделать так, чтобы числа чередовались, т.е. получился такой вывод:
     * Поток 1:1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4....
     * Поток 2: 1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3....
     * Всегда должен начинать Поток 1.
     */

    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    private static final int THREAD_COUNT = 2;
    private static final int START_VALUE = 1;
    private static final int END_VALUE = 10;

    private int currentValue = START_VALUE;
    private Direction direction = Direction.UP;
    private String lastThread = "Thread-2";
    private int amountThreadAction = 0;

    public static void main(String[] args) {
        Demo demo = new Demo();
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(demo::action, "Thread-" + (i + 1)).start();
        }
    }

    private synchronized void action() {
        while (!Thread.currentThread().isInterrupted()) {
            String threadName = Thread.currentThread().getName();
            try {
                while (lastThread.equals(threadName)) {
                    this.wait();
                }

                logger.info("Thread: {}, count: {}", threadName, currentValue);

                lastThread = threadName;

                // Счетчик потоков, сколько из них выполнили action. Если два -> обнуляем счетчик, изменяем currentValue
                amountThreadAction += 1;
                if (amountThreadAction >= THREAD_COUNT) {
                    amountThreadAction = 0;
                    getNextValue();
                }

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void getNextValue() {
        // Когда currentValue == 1, ставим direction вверх. Если currentValue == 10 -> вниз
        if (currentValue == END_VALUE) {
            direction = Direction.DOWN;
        } else if (currentValue == START_VALUE) {
            direction = Direction.UP;
        }

        // Обновляем значение currentValue
        switch (direction) {
            case UP -> currentValue++;
            case DOWN -> currentValue--;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
