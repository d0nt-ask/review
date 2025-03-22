package io.whatap.order.order.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CreateOrderCommand {
    private List<CreateOrderProductCommand> products;
}
