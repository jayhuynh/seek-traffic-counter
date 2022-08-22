package seek.traffic.counter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public class Counter {
    private int total = 0;
    private HashMap<String, Integer> days = new HashMap<>();
    private List<SimpleEntry<String, Integer>> topThreeRecords = new ArrayList<>(Collections.nCopies(3, new SimpleEntry<>("", 0)));
    private List<SimpleEntry<String, Integer>> leastThreeContiguousRecords = new ArrayList<>(Collections.nCopies(3, new SimpleEntry<>("", Integer.MAX_VALUE)));

    public void loadFile(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            Queue<SimpleEntry<String, Integer>> records = new LinkedList<>();
            String line = reader.readLine();

            while (line != null) {
                processLine(line, records);
                line = reader.readLine();
            }

            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }

    private void processLine(String line, Queue<SimpleEntry<String, Integer>> records) {
        String[] parts = line.split(" ");
        String timestamp = parts[0];
        String day = timestamp.split("T")[0];
        int count = Integer.parseInt(parts[1]);

        total += count;

        if (days.containsKey(day)) {
            days.put(day, days.get(day) + count);
        } else {
            days.put(day, count);
        }

        for (int i = 0; i < topThreeRecords.size(); i++) {
            if (count > topThreeRecords.get(i).getValue()) {
                topThreeRecords.set(i, new SimpleEntry<>(timestamp, count));
                break;
            }
        }

        records.add(new SimpleEntry<>(timestamp, count));
        if (records.size() > 3) {
            records.poll();
        }

        if (records.size() == 3 && isContiguous((List) records)) {
            int sum = records.stream().mapToInt(SimpleEntry::getValue).sum();
            if (sum < leastThreeContiguousRecords.stream().mapToInt(SimpleEntry::getValue).sum()) {
                leastThreeContiguousRecords = new ArrayList<>(records);
            }
        }
    }

    private boolean isContiguous(List<SimpleEntry<String, Integer>> records) {
        for (int i = 0; i < records.size() - 1; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime currentDay = LocalDateTime.parse(records.get(i).getKey(), formatter);
            LocalDateTime nextDay = LocalDateTime.parse(records.get(i + 1).getKey(), formatter);

            if (Duration.between(currentDay, nextDay).toMinutes() != 30) {
                return false;
            }
        }
        return true;
    }

    public List<SimpleEntry<String, Integer>> getLeastThreeContiguousRecords() {
        return leastThreeContiguousRecords;
    }

    public int getTotal() {
        return total;
    }

    public HashMap<String, Integer> getDays() {
        return days;
    }

    public List<SimpleEntry<String, Integer>> getTopThreeRecords() {
        return topThreeRecords;
    }
}
