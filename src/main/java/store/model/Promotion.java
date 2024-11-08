package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String promotionName) {
        this.name = promotionName;
        this.buy = 0;
        this.get = 0;
        this.startDate = null;
        this.endDate = null;
    }

    public Promotion(String name, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive() {
        LocalDate today = DateTimes.now().toLocalDate();
        return !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
