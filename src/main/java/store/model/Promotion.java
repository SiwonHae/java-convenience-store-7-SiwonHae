package store.model;

import java.time.LocalDate;

public record Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
    private boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
