package io.whatap.order.order.event;

import io.whatap.order.order.entity.Order;
import lombok.Getter;

@Getter
public class CreatedOrderEvent {
    private Order order;

    private CreatedOrderEvent(Order order) {
        this.order = order;
    }

    public static CreatedOrderEvent from(Order order) {
        return new CreatedOrderEvent(order);
    }
}
