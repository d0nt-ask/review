package io.whatap.order.order.controller.req;

import io.whatap.library.shared.web.Command;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrderProductCommand extends Command {
    private List<CreateOrderProductCommand> products;

    public void validate() {
        if (CollectionUtils.isEmpty(products)) {
            throw new IllegalArgumentException("상품목록은 필수 입력값입니다.");
        }

        for (CreateOrderProductCommand product : products) {
            product.validate();
        }
    }
}
