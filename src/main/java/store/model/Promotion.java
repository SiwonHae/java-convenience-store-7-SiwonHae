package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public record Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
    private boolean isActive() {
        LocalDate today = DateTimes.now().toLocalDate();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }
}
