package io.whatap.order.order.controller.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChangeOrderCommand {
    private String roadAddr;
    private String jibunAddr;
    private String detailAddr;
}
