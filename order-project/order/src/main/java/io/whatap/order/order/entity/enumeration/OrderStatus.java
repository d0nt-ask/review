package io.whatap.order.order.entity.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {
    DRAFT(true, true),
    PAID(true, false),
    PREPARING(false, false),
    SHIPPED(false, false),
    DELIVERED(false, false),
    CANCELLED(false, false),
    CANCEL_REQUESTED(false, false);

    private final boolean editable;
    private final boolean deletable;

}
