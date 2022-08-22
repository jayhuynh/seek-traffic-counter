package seek.traffic.counter;

import java.util.AbstractMap;

public class App {
    public static void main(String[] args) {
        Counter counter = new Counter();
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        counter.loadFile("src/test/resources/traffic.txt");


        System.out.println("Total cars: " + counter.getTotal());
        System.out.println("Total cars in day: ");
        for (String day : counter.getDays().keySet()) {
            System.out.println(day + " " + counter.getDays().get(day));
        }
        System.out.println("Top 3 half hours with most cars: ");
        for (AbstractMap.SimpleEntry<String, Integer> record : counter.getTopThreeRecords()) {
            System.out.println(record.getKey() + " " + record.getValue());
        }
        System.out.println("3 contiguous half hours with least cars: ");
        for (AbstractMap.SimpleEntry<String, Integer> record : counter.getLeastThreeContiguousRecords()) {
            System.out.println(record.getKey() + " " + record.getValue());
        }
    }
}
