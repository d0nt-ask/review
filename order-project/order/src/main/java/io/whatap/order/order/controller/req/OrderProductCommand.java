package io.whatap.order.order.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderProductCommand {
    private String roadAddr; // 도로명주소 전체
    private String jibunAddr; // 지번 주소
    private String detailAddr;
    private List<CreateOrderProductCommand> products;
}
