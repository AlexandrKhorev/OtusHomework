package homework;


import java.util.Map;
import java.util.TreeMap;
import java.util.AbstractMap;
import java.util.NavigableMap;


public class CustomerService {
    /*
     * Customer сравнивается по score, добавленные объекты в NavigableMap будут также отсортированы по score.
     * У NavigableMap будут полезными методы firstKey (для получения ключа с минимальным значением score),
     * и higherKey (для получения большего от переданного ключа).
     * Судя по тестам, getSmallest и getNext должны возвращать копии объектов, чтобы изменение этих копий не затронуло
     * CustomerService и исходные объекты.
     */

    NavigableMap<Customer, String> customersData = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return copyObject(customersData.firstKey());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyObject(customersData.higherKey(customer));
    }

    private Map.Entry<Customer, String> copyObject(Customer customerKey) {
        if (customerKey == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(
                new Customer(customerKey.getId(), customerKey.getName(), customerKey.getScores()),
                customersData.get(customerKey)
        );
    }

    public void add(Customer customer, String data) {
        customersData.put(customer, data);
    }
}
