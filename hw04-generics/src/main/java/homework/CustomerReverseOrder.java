package homework;


import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    /*
     * Можно использовать реализацию интерфейса Deque (Работает по принципу LIFO - последний пришел, первый ушел) - ArrayDeque
     * Используются методы addLast - добавление в конец очереди, pollLast - возвращение элемента с его удалением.
     * Когда очередь опустеет, при попытке забрать элемент - будет возвращаться null.
     */
    Deque<Customer> customersQueue = new ArrayDeque<>();

    public void add(Customer customer) {
        customersQueue.addLast(customer);
    }

    public Customer take() {
        return customersQueue.pollLast();
    }
}
