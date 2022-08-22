package seek.traffic.counter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CounterTest {
    @Test
    void shouldThrowExceptionWhenInvalidFilePath() {
        Counter counter = new Counter();
        assertThrows(RuntimeException.class, () -> counter.loadFile("invalid/path"));
    }

    @Test
    void shouldCountTotalCars() {
        Counter counter = new Counter();
        counter.loadFile("src/test/resources/traffic.txt");

        assertEquals(398, counter.getTotal());
    }

    @Test
    void shouldCountTotalCarsInADay() {
        Counter counter = new Counter();
        counter.loadFile("src/test/resources/traffic.txt");

        assertEquals(81, counter.getDays().get("2021-12-05"));
    }

    @Test
    void shouldHasTop3HalfHoursWithMostCar() {
        Counter counter = new Counter();
        counter.loadFile("src/test/resources/traffic.txt");

        assertEquals("2021-12-01T07:30:00", counter.getTopThreeRecords().get(0).getKey());
        assertEquals("2021-12-01T08:00:00", counter.getTopThreeRecords().get(1).getKey());
        assertEquals("2021-12-08T18:00:00", counter.getTopThreeRecords().get(2).getKey());
    }

    @Test
    void shouldHasLeast3ContiguousHalfHours() {
        Counter counter = new Counter();
        counter.loadFile("src/test/resources/traffic.txt");

        assertEquals("2021-12-01T05:00:00", counter.getLeastThreeContiguousRecords().get(0).getKey());
        assertEquals("2021-12-01T05:30:00", counter.getLeastThreeContiguousRecords().get(1).getKey());
        assertEquals("2021-12-01T06:00:00", counter.getLeastThreeContiguousRecords().get(2).getKey());
    }
}
