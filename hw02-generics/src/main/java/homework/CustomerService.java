package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> treeMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return Map.entry(Customer.copyOf(treeMap.firstEntry().getKey()), treeMap.firstEntry().getValue()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = treeMap.higherEntry(customer);
        return higherEntry != null ? Map.entry(Customer.copyOf(higherEntry.getKey()), higherEntry.getValue()) : null; // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        treeMap.put(customer, data);
    }
}
