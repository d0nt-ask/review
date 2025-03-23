package io.whatap.order.event;


import io.whatap.order.event.dto.OrderProductDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeletedOrderEvent {

    private List<OrderProductDto> orderProducts;

}
