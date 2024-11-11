package store.model;

import java.util.List;

public record TotalPrice(int price) {
    public TotalPrice(List<OrderInfo> orderInfos) {
        this(calculateTotalPrice(orderInfos));
    }

    private static int calculateTotalPrice(List<OrderInfo> orderInfos) {
        return orderInfos.stream()
                .mapToInt(OrderInfo::price)
                .sum();
    }
}
