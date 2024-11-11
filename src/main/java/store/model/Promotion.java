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

    public int calculateBonusQuantity(int orderQuantity) {
        if (buy == 0 || get == 0 || orderQuantity < buy) {
            return 0;
        }
        int bonusQuantity = orderQuantity / (buy + get);
        int remainQuantity = orderQuantity % (buy + get);
        if (remainQuantity >= buy) {
            bonusQuantity += get;
        }

        return bonusQuantity;
    }

    public boolean canReceivePromotion(int orderQuantity) {
        if (buy == 0 || get == 0) {
            return false;
        }
        int remainder = orderQuantity % (buy + get);
        return remainder == buy;
    }

    public int calculateNonPromotionQuantity(int orderQuantity, int promotionQuantity) {
        if (buy == 0 || get == 0) {
            return orderQuantity;
        }

        int stockSet = promotionQuantity / (buy + get);

        if (orderQuantity >= promotionQuantity) {
            return orderQuantity - (buy + get) * stockSet;
        }

        return 0;
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    @Override
    public String toString() {
        return name;
    }
}
